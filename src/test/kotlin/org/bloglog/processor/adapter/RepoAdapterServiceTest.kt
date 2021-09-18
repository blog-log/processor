package org.bloglog.processor.adapter

import org.bloglog.processor.service.DocumentManagerService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class RepoAdapterServiceTest {
    private lateinit var adapter: RepoAdapter

    @BeforeEach
    fun setUp() {
        adapter = RepoAdapterService()
    }

    @Test
    fun repoNameToId() {
        val repoName = "https://github.com/brandon/personal-thing"

        val repoId = adapter.repoNameToId(repoName)

        assertEquals("https:--github.com-brandon-personal-thing", repoId)
    }
}