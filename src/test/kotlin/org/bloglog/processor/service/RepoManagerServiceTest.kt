package org.bloglog.processor.service

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class RepoManagerServiceTest {
    private lateinit var service : RepoManagerService

    @BeforeEach
    fun setUp() {
        service = RepoManagerService()
    }

    @Test
    fun set() {
        val request = RepoSetRequest(
            "fake",
            listOf("fake"),
            listOf("fake"),
        )

        val resp = service.set(request)

        assertEquals(200, resp.status)
    }

    @Test
    fun delete() {
        val request = RepoDeleteRequest(
            "fake"
        )

        val resp = service.delete(request)

        assertEquals(200, resp.status)
    }
}