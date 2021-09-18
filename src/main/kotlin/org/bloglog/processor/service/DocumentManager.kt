package org.bloglog.processor.service

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.bloglog.processor.adapter.RepoAdapter
import org.bloglog.processor.handler.Adder
import org.bloglog.processor.model.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface DocumentManager {
    fun set(request: DocumentSetRequest): DocumentSetResponse
    fun delete(request: DocumentDeleteRequest): DocumentDeleteResponse
    fun search(request: DocumentSearchRequest): DocumentSearchResponse
}

data class DocumentSetRequest(
    val id: String,
    val title: String,
    val repo: String,
    val branch: String,
    val path: String,
)

data class DocumentSetResponse(
    val status: Int,
    val message: String,
    val data: Document,
    val error: List<String>?,
)

data class DocumentDeleteRequest(
    val id: String,
)

data class DocumentDeleteResponse(
    val status: Int,
    val message: String,
    val data: DocumentDeleteResponseData,
    val error: List<String>?,
)

data class DocumentDeleteResponseData(
    val id: String
)

data class DocumentSearchRequest(
    val repos: List<String>,
)

data class DocumentSearchResponse(
    val status: Int,
    val message: String,
    val data: List<Document>,
    val error: List<String>?,
)

@Service
class DocumentManagerService : DocumentManager {
    private val client = OkHttpClient()

    private val json = "application/json; charset=utf-8".toMediaType()

    private val gson = Gson()



    override fun set(request: DocumentSetRequest): DocumentSetResponse {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("us-central1-bloglog-dev.cloudfunctions.net")
            .addPathSegment("bloglog-database-dev-documentSet")
            .build()

        val reqBody = gson.toJson(request, DocumentSetRequest::class.java).toRequestBody(json)

        val request: Request = Request.Builder()
            .url(url)
            .post(reqBody)
            .build()

        val respBody = client.newCall(request).execute().body

        val tmp = respBody?.string()

        return gson.fromJson(tmp, DocumentSetResponse::class.java)
    }

    override fun delete(request: DocumentDeleteRequest): DocumentDeleteResponse {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("us-central1-bloglog-dev.cloudfunctions.net")
            .addPathSegment("bloglog-database-dev-documentRemove")
            .build()

        val reqBody = gson.toJson(request, DocumentDeleteRequest::class.java).toRequestBody(json)

        val request: Request = Request.Builder()
            .url(url)
            .post(reqBody)
            .build()

        val respBody = client.newCall(request).execute().body

        val tmp = respBody?.string()

        return gson.fromJson(tmp, DocumentDeleteResponse::class.java)
    }

    override fun search(request: DocumentSearchRequest): DocumentSearchResponse {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("us-central1-bloglog-dev.cloudfunctions.net")
            .addPathSegment("bloglog-database-dev-documentSearch")
            .build()

        val reqBody = gson.toJson(request, DocumentSearchRequest::class.java).toRequestBody(json)

        val request: Request = Request.Builder()
            .url(url)
            .post(reqBody)
            .build()

        val respBody = client.newCall(request).execute().body

        val tmp = respBody?.string()

        return gson.fromJson(tmp, DocumentSearchResponse::class.java)
    }
}