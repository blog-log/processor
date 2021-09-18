package org.bloglog.processor.adapter

import org.springframework.stereotype.Service

interface RepoAdapter {
    fun repoNameToId(repoName: String): String
}

@Service
class RepoAdapterService : RepoAdapter {
    override fun repoNameToId(repoName: String): String {
        return repoName.replace("/", "-")
    }
}