package com.example.engine.ocr

import android.content.Context
import android.graphics.Bitmap
import com.googlecode.tesseract.android.TessBaseAPI
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.ImageType
import com.tom_roush.pdfbox.rendering.PDFRenderer
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/**
 * On-demand, offline-after-first-download Sinhala OCR for scanned PDFs.
 * The model is downloaded from a pinned official tessdata_fast revision and
 * checked against its Git blob SHA-1 before it is used.
 */
object SinhalaScannedPdfOcr {
    private const val MODEL_URL =
        "https://raw.githubusercontent.com/tesseract-ocr/tessdata_fast/87416418657359cb625c412a48b6e1d6d41c29bd/sin.traineddata"
    private const val MODEL_BYTES = 1_727_413L
    private const val MODEL_GIT_BLOB_SHA1 = "d0c8584e78c44666efcd7fbd47940a8a1121550d"
    private const val RENDER_DPI = 160f
    private const val MAX_PAGES = 500

    fun extract(
        context: Context,
        pdf: PDDocument,
        onModelDownload: () -> Unit = {},
        onPageProgress: (completed: Int, total: Int) -> Unit = { _, _ -> }
    ): String {
        require(pdf.numberOfPages in 1..MAX_PAGES) {
            "Sinhala scanned-PDF OCR supports 1–$MAX_PAGES pages per import."
        }
        val model = ensureModel(context, onModelDownload)
        val api = TessBaseAPI()
        try {
            val dataRoot = model.parentFile?.parentFile
                ?: error("Sinhala OCR model directory is invalid.")
            check(api.init(dataRoot.absolutePath, "sin", TessBaseAPI.OEM_LSTM_ONLY)) {
                "Sinhala OCR could not initialize. Please retry the model download."
            }
            api.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO)
            val renderer = PDFRenderer(pdf)
            val output = StringBuilder()
            val total = pdf.numberOfPages
            for (index in 0 until total) {
                val bitmap = renderer.renderImageWithDPI(index, RENDER_DPI, ImageType.RGB)
                try {
                    api.setImage(bitmap)
                    val pageText = api.getUTF8Text().orEmpty().trim()
                    if (pageText.isNotBlank()) {
                        if (output.isNotEmpty()) output.append("\n\n")
                        output.append(pageText)
                    }
                    api.clear()
                } finally {
                    bitmap.recycle()
                }
                onPageProgress(index + 1, total)
            }
            return output.toString().trim()
        } finally {
            api.recycle()
        }
    }

    private fun ensureModel(context: Context, onModelDownload: () -> Unit): File {
        val root = File(context.filesDir, "skeleton_ocr")
        val tessdata = File(root, "tessdata")
        if (!tessdata.exists() && !tessdata.mkdirs()) {
            error("Could not create private Sinhala OCR model directory.")
        }
        val model = File(tessdata, "sin.traineddata")
        if (model.isFile && model.length() == MODEL_BYTES && hasExpectedGitHash(model)) return model

        onModelDownload()
        val partial = File(tessdata, "sin.traineddata.part")
        if (partial.exists()) partial.delete()
        val connection = (URL(MODEL_URL).openConnection() as HttpURLConnection).apply {
            connectTimeout = 20_000
            readTimeout = 45_000
            instanceFollowRedirects = true
            useCaches = false
            requestMethod = "GET"
        }
        try {
            check(connection.responseCode == HttpURLConnection.HTTP_OK) {
                "Sinhala OCR model download failed (HTTP ${connection.responseCode}). Check your internet connection."
            }
            connection.inputStream.use { input ->
                FileOutputStream(partial).use { output ->
                    val buffer = ByteArray(32 * 1024)
                    var total = 0L
                    while (true) {
                        val count = input.read(buffer)
                        if (count < 0) break
                        total += count
                        check(total <= MODEL_BYTES) { "Downloaded Sinhala OCR model has an unexpected size." }
                        output.write(buffer, 0, count)
                    }
                    output.fd.sync()
                }
            }
        } finally {
            connection.disconnect()
        }
        check(partial.length() == MODEL_BYTES && hasExpectedGitHash(partial)) {
            partial.delete()
            "Sinhala OCR model verification failed. Please try again."
        }
        if (model.exists() && !model.delete()) {
            partial.delete()
            error("Could not replace an invalid Sinhala OCR model.")
        }
        check(partial.renameTo(model)) {
            partial.delete()
            "Could not save the Sinhala OCR model on this device."
        }
        return model
    }

    private fun hasExpectedGitHash(file: File): Boolean {
        val digest = MessageDigest.getInstance("SHA-1")
        digest.update("blob ${file.length()}\u0000".toByteArray(Charsets.UTF_8))
        file.inputStream().use { input ->
            val buffer = ByteArray(32 * 1024)
            while (true) {
                val count = input.read(buffer)
                if (count < 0) break
                digest.update(buffer, 0, count)
            }
        }
        val actual = digest.digest().joinToString("") { "%02x".format(it) }
        return actual == MODEL_GIT_BLOB_SHA1
    }
}
