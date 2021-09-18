package org.bloglog.processor.handler

import org.bloglog.processor.adapter.RepoAdapter
import org.bloglog.processor.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.StringBuilder

interface Adder {
    fun add(request: AddRequest): AddResponse
}

data class AddRequest(
    val repos: List<String>
)

data class AddResponse(
    val success: Boolean
)

@Service
class AddHandler : Adder {
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

    override fun add(request: AddRequest): AddResponse {

        // do work on each repo in list
        request.repos.map {

            try {
                // extract metainfo from repo
                val extractResponse = extractorService.extract(ExtractRequest(it))

                // get current documents for repo in database
                val documentSearchResponse = documentManagerService.search(DocumentSearchRequest(listOf(it)))

                // construct remove list
                val documentRemoveList = documentSearchResponse.data.filter { searchDocument ->
                    extractResponse.data.any { extractDocument -> extractDocument.title == searchDocument.title }
                }

                // add needed documents to database and search
                extractResponse.data.forEach { extractData ->

                    val docId = StringBuilder()
                        .append(it)
                        .append("/")
                        .append(extractResponse.branch)
                        .append("/")
                        .append(extractData.path)
                        .toString()
                    // add to database
                    documentManagerService.set(
                        DocumentSetRequest(
                            repoAdapter.repoNameToId(docId),
                            extractData.title,
                            extractResponse.repo,
                            extractResponse.branch,
                            extractData.path,
                        )
                    )

                    // add to search
                    searchManagerService.set(
                        SearchSetRequest(
                            repoAdapter.repoNameToId(docId),
                            extractData.title,
                            extractResponse.repo,
                            extractResponse.branch,
                            extractData.path,
                        )
                    )
                }

                // remove documents from database and search
                documentRemoveList.forEach { documentRemove ->
                    // remove from database
                    documentManagerService.delete(DocumentDeleteRequest(documentRemove.id))

                    // remove from search
                    searchManagerService.delete(SearchDeleteRequest(documentRemove.id))
                }

                // set repo meta info and status
                repoManagerService.set(
                    RepoSetRequest(
                        repoAdapter.repoNameToId(it),
                        if (extractResponse.warning != "") listOf(extractResponse.warning) else null,
                        null
                    )
                )
            } catch (e: Exception) {
                repoManagerService.set(RepoSetRequest(repoAdapter.repoNameToId(it), null, listOf(e.toString())))

                return AddResponse(false)
            }
        }
        return AddResponse(true)
    }

}