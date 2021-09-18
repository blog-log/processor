package org.bloglog.processor.handler

import org.bloglog.processor.adapter.RepoAdapter
import org.bloglog.processor.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface Remover {
    fun remove(request: RemoveRequest): RemoveResponse
}

data class RemoveRequest(
    val repos: List<String>
)

data class RemoveResponse(
    val success: Boolean
)

@Service
class RemoveHandler : Remover {
    @Autowired
    lateinit var extractorService: Extractor

    @Autowired
    lateinit var documentManagerService: DocumentManager

    @Autowired
    lateinit var repoManagerService: RepoManager

    @Autowired
    lateinit var searchManagerService: SearchManager

    @Autowired
    lateinit var repoAdapter: RepoAdapter

    override fun remove(request: RemoveRequest): RemoveResponse {

        // do work on each repo in list
        request.repos.map {
            try {
                val documentSearchResponse = documentManagerService.search(DocumentSearchRequest(listOf(it)))

                documentSearchResponse.data.forEach { document ->
                    // remove document from database
                    val documentDeleteResponse = documentManagerService.delete(DocumentDeleteRequest(document.id))

                    // remove document from search
                    val searchDeleteResponse = searchManagerService.delete(SearchDeleteRequest(document.id))
                }

                val repoDeleteResponse = repoManagerService.delete(RepoDeleteRequest(repoAdapter.repoNameToId(it)))
            } catch (e: Exception) {
                repoManagerService.set(
                    RepoSetRequest(
                        repoAdapter.repoNameToId(it),
                        null,
                        listOf(e.toString())
                    )
                )

                return RemoveResponse(false)
            }
        }
        return RemoveResponse(true)
    }
}