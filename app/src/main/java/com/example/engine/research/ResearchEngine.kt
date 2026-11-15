package com.example.engine.research

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Free metadata discovery for SKELETON.
 *
 * This service retrieves bibliographic records only. It does not treat a search
 * result, citation count, or publisher record as proof that a claim is true.
 * It makes no AI/model calls and requires no API key.
 */
data class ResearchSource(
  val title: String,
  val authors: List<String>,
  val publishedYear: Int?,
  val publisher: String?,
  val doi: String?,
  val url: String,
  val sourceType: String = "Bibliographic record",
  val evidenceNote: String = "Metadata discovery only — inspect the source before relying on its claims."
)

sealed class ResearchSearchResult {
  data class Success(val query: String, val sources: List<ResearchSource>) : ResearchSearchResult()
  data class Unavailable(val message: String) : ResearchSearchResult()
}

object ResearchEngine {
  private const val CROSSREF_ENDPOINT = "https://api.crossref.org/works"
  private const val DEFAULT_LIMIT = 8
  private const val CONNECT_TIMEOUT_MS = 7_000
  private const val READ_TIMEOUT_MS = 9_000

  suspend fun search(query: String, limit: Int = DEFAULT_LIMIT): ResearchSearchResult =
    withContext(Dispatchers.IO) {
      val cleanQuery = query.trim().take(180)
      if (cleanQuery.isBlank()) {
        return@withContext ResearchSearchResult.Unavailable("Enter a topic to search.")
      }

      var connection: HttpURLConnection? = null
      try {
        val encoded = URLEncoder.encode(cleanQuery, "UTF-8")
        val safeLimit = limit.coerceIn(1, 15)
        connection = (URL("$CROSSREF_ENDPOINT?query.bibliographic=$encoded&rows=$safeLimit").openConnection() as HttpURLConnection).apply {
          requestMethod = "GET"
          connectTimeout = CONNECT_TIMEOUT_MS
          readTimeout = READ_TIMEOUT_MS
          setRequestProperty("Accept", "application/json")
          setRequestProperty("User-Agent", "SKELETON/2.0 (free bibliographic discovery)")
        }

        val status = connection.responseCode
        if (status !in 200..299) {
          return@withContext ResearchSearchResult.Unavailable("The research index returned HTTP $status. Try again later.")
        }

        val body = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        val message = JSONObject(body).optJSONObject("message")
          ?: return@withContext ResearchSearchResult.Unavailable("The research index returned an unreadable response.")
        val items = message.optJSONArray("items") ?: JSONArray()
        val sources = buildList {
          for (index in 0 until items.length()) {
            val item = items.optJSONObject(index) ?: continue
            val title = item.optJSONArray("title")?.optString(0).orEmpty().trim()
            if (title.isBlank()) continue

            val authors = item.optJSONArray("author").toAuthorNames()
            val year = extractYear(item)
            val doi = item.optString("DOI").takeIf { it.isNotBlank() && it != "null" }
            val recordUrl = item.optString("URL").takeIf { it.startsWith("https://") }
              ?: doi?.let { "https://doi.org/$it" }
              ?: continue
            val publisher = item.optString("publisher").takeIf { it.isNotBlank() && it != "null" }

            add(
              ResearchSource(
                title = title,
                authors = authors,
                publishedYear = year,
                publisher = publisher,
                doi = doi,
                url = recordUrl,
                sourceType = classifyType(item.optString("type"))
              )
            )
          }
        }
        ResearchSearchResult.Success(cleanQuery, sources)
      } catch (_: java.net.SocketTimeoutException) {
        ResearchSearchResult.Unavailable("The research index took too long to respond. Your saved knowledge is still available offline.")
      } catch (_: java.io.IOException) {
        ResearchSearchResult.Unavailable("Research search is offline. Check your connection and try again.")
      } catch (_: Exception) {
        ResearchSearchResult.Unavailable("The research index could not be read. Try a simpler search.")
      } finally {
        connection?.disconnect()
      }
    }

  private fun JSONArray?.toAuthorNames(): List<String> {
    if (this == null) return emptyList()
    return buildList {
      for (index in 0 until length()) {
        val author = optJSONObject(index) ?: continue
        val name = listOf(author.optString("given"), author.optString("family"))
          .filter { it.isNotBlank() && it != "null" }
          .joinToString(" ")
        if (name.isNotBlank()) add(name)
      }
    }
  }

  private fun extractYear(item: JSONObject): Int? {
    val dateKeys = listOf("published-print", "published-online", "published", "created")
    for (key in dateKeys) {
      val parts = item.optJSONObject(key)?.optJSONArray("date-parts")
        ?.optJSONArray(0) ?: continue
      val year = parts.optInt(0, 0)
      if (year in 1000..3000) return year
    }
    return null
  }

  private fun classifyType(type: String): String = when (type.lowercase()) {
    "journal-article" -> "Journal article"
    "book", "monograph" -> "Book"
    "book-chapter" -> "Book chapter"
    "proceedings-article" -> "Conference paper"
    "posted-content" -> "Preprint or posted work"
    "report" -> "Report"
    "dissertation" -> "Dissertation"
    else -> "Bibliographic record"
  }
}
