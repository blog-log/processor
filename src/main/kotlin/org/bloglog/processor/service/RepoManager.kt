package org.bloglog.processor.service

import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.bloglog.processor.client.ignoreAllSSLErrors
import org.bloglog.processor.model.Document
import org.bloglog.processor.model.Repo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.validation.constraints.NotBlank

interface RepoManager {
    fun set(request: RepoSetRequest): RepoSetResponse
    fun delete(request: RepoDeleteRequest): RepoDeleteResponse
}

data class RepoSetRequest(
    val id: String,
    val warning: List<String>?,
    val error: List<String>?,
)

data class RepoSetResponse(
    val status: Int,
    val message: String,
    val data: Repo,
    val error: List<String>,
)

data class RepoDeleteRequest(
    val id: String,
)

data class RepoDeleteResponse(
    val status: Int,
    val message: String,
    val data: RepoDeleteResponseData,
    val error: List<String>,
)

data class RepoDeleteResponseData (
    val id: String
)

@Service
class RepoManagerService(
    @Value("\${DATASTORE_URL}")
    private val serviceUrl: String
) : RepoManager {
    private val client = OkHttpClient.Builder().ignoreAllSSLErrors().build()

    private val json = "application/json; charset=utf-8".toMediaType()

    private val gson = Gson()

    override fun set(request: RepoSetRequest): RepoSetResponse {
        val url = serviceUrl.toHttpUrl()
            .newBuilder()
            .addPathSegment("repo")
            .build()

        val reqBody = gson.toJson(request, RepoSetRequest::class.java).toRequestBody(json)

        val request: Request = Request.Builder()
            .url(url)
            .post(reqBody)
            .build()

        val respBody = client.newCall(request).execute().body

        val tmp = respBody?.string()

        return gson.fromJson(tmp, RepoSetResponse::class.java)
    }

    override fun delete(request: RepoDeleteRequest): RepoDeleteResponse {
        val url = serviceUrl.toHttpUrl()
            .newBuilder()
            .addPathSegment("repo")
            .addPathSegment(request.id)
            .build()

        val request: Request = Request.Builder()
            .url(url)
            .delete()
            .build()

        val respBody = client.newCall(request).execute().body

        val tmp = respBody?.string()

        return gson.fromJson(tmp, RepoDeleteResponse::class.java)
    }
}