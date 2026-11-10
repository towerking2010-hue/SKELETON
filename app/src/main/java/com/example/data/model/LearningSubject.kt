package com.example.data.model

/**
 * First-class subject filters for the learning feed.
 * Classification is derived from existing card metadata, so this does not require a Room migration.
 */
enum class LearningSubject(val label: String, private val keywords: List<String>) {
  ALL("All", emptyList()),
  SCIENCE("Science", listOf("physics", "chemistry", "biology", "cellular", "molecular", "neuro", "thermodynamic", "genetic")),
  MATHEMATICS("Mathematics", listOf("math", "arithmetic", "geometry", "calculus", "algebra", "number theory", "fibonacci", "ratio")),
  HISTORY("History", listOf("history", "historical", "civilization", "ancient", "empire", "archaeology")),
  TECHNOLOGY_AI("Technology & AI", listOf("technology", "computer", "software", "programming", "ai", "machine learning", "network", "blockchain", "data")),
  PSYCHOLOGY("Psychology", listOf("psychology", "cognitive", "behavior", "behaviour", "neuroscience", "manipulation", "memory")),
  BUSINESS_ECONOMICS("Business & Economics", listOf("business", "economics", "finance", "market", "capital", "trade")),
  ASTRONOMY("Astronomy", listOf("astronomy", "astrophysics", "cosmos", "universe", "planet", "stellar", "space")),
  PHILOSOPHY("Philosophy", listOf("philosophy", "metaphysics", "ontology", "logic", "epistemology", "ethics")),
  LINGUISTICS("Linguistics", listOf("linguistic", "language", "etymology", "grammar", "trivium", "rhetoric", "phonetic")),
  MYTHOLOGY_ESOTERIC("Mythology & Esoteric", listOf("mythology", "myth", "hermetic", "alchemy", "gematria", "numerology", "masonry", "sacred geometry")),
  ARTS_DESIGN("Arts & Design", listOf("music", "acoustic", "colour", "color", "visual", "composition", "design", "art"));

  fun matches(card: FeedCard): Boolean {
    if (this == ALL) return true
    val searchable = "${card.title} ${card.branchDomain} ${card.crossLinkTarget.orEmpty()}".lowercase()
    return keywords.any { keyword -> searchable.contains(keyword) }
  }
}
