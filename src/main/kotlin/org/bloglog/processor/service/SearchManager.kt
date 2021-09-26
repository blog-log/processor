package org.bloglog.processor.service

import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.bloglog.processor.client.ignoreAllSSLErrors
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface SearchManager {
    fun set(request: SearchSetRequest): SearchSetResponse
    fun delete(request: SearchDeleteRequest): SearchDeleteResponse
}

data class SearchSetRequest(
    val id: String,
    val title: String,
    val repo: String,
    val branch: String,
    val path: String,
)

data class SearchSetResponse(
    val id: String,
    val title: String,
    val repo: String,
    val branch: String,
    val path: String,
)

data class SearchDeleteRequest(
    val id: String,
)

data class SearchDeleteResponse(
    val id: String,
)

@Service
class SearchManagerService(
    @Value("\${SEARCHER_URL}")
    private val serviceUrl: String
) : SearchManager {
    private val client = OkHttpClient.Builder().ignoreAllSSLErrors().build()

    private val json = "application/json; charset=utf-8".toMediaType()

    private val gson = Gson()

    override fun set(request: SearchSetRequest): SearchSetResponse {
        val url = serviceUrl.toHttpUrl()
            .newBuilder()
            .addPathSegment("document")
            .build()

        val reqBody = gson.toJson(request, SearchSetRequest::class.java).toRequestBody(json)

        val request: Request = Request.Builder()
            .url(url)
            .post(reqBody)
            .build()

        val respBody = client.newCall(request).execute().body

        val tmp = respBody?.string()

        return gson.fromJson(tmp, SearchSetResponse::class.java)
    }

    override fun delete(request: SearchDeleteRequest): SearchDeleteResponse {
        val url = serviceUrl.toHttpUrl()
            .newBuilder()
            .addPathSegment("document")
            .addPathSegment(request.id)
            .build()

        val request: Request = Request.Builder()
            .url(url)
            .delete()
            .build()

        val respBody = client.newCall(request).execute().body

        val tmp = respBody?.string()

        return gson.fromJson(tmp, SearchDeleteResponse::class.java)
    }
}