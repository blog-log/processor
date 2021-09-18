package org.bloglog.processor.service

import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import java.io.IOException
import javax.validation.constraints.NotBlank

interface Extractor {
    fun extract(request: ExtractRequest): ExtractResponse
}

data class ExtractRequest(
    val repo: String
)

data class ExtractResponse(
    val repo: String,
    val branch: String,
    val data: List<ExtractResponseData>,
    val error: List<String>,
    val warning: String,
)

data class ExtractResponseData (
    val title: String,
    val path: String,
)

@Service
class ExtractorService : Extractor {
    private val client = OkHttpClient()

    private val gson = Gson()

    @Throws(IOException::class)
    override fun extract(request: ExtractRequest): ExtractResponse {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("us-central1-bloglog-dev.cloudfunctions.net")
            .addPathSegment("bloglog-extractor-dev-extractor")
            .addQueryParameter("repo", request.repo)
            .build()

        val request: Request = Request.Builder()
            .url(url)
            .build()
        val body = client.newCall(request).execute().body

        return gson.fromJson(body?.string(), ExtractResponse::class.java)
    }
}

