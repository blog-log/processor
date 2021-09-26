package org.bloglog.processor.service

import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bloglog.processor.client.ignoreAllSSLErrors
import org.springframework.beans.factory.annotation.Value
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

data class ExtractResponseData(
    val title: String,
    val path: String,
)

@Service
class ExtractorService(
    @Value("\${EXTRACTOR_URL}")
    private val serviceUrl: String
) : Extractor {
    private val client = OkHttpClient.Builder().ignoreAllSSLErrors().build()

    private val gson = Gson()

    @Throws(IOException::class)
    override fun extract(request: ExtractRequest): ExtractResponse {
        val url = serviceUrl.toHttpUrl()
            .newBuilder()
            .addPathSegment("extract")
            .addQueryParameter("repo", request.repo)
            .build()

        val request: Request = Request.Builder()
            .url(url)
            .build()
        val respBody = client.newCall(request).execute().body

        val tmp = respBody?.string()

        return gson.fromJson(tmp, ExtractResponse::class.java)
    }
}

