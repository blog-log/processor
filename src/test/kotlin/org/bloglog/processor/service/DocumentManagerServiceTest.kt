package org.bloglog.processor.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class DocumentManagerServiceTest {
    private lateinit var service : DocumentManagerService

    @BeforeEach
    fun setUp() {
        service = DocumentManagerService()
    }

    @Test
    fun set_success() {
        val request = DocumentSetRequest(
            "fake",
            "fake",
            "fake",
            "fake",
            "fake",
        )

        val resp = service.set(request)

        assertEquals(200, resp.status)
    }

    @Test
    fun delete_success() {
        val request = DocumentDeleteRequest("fake")

        val resp = service.delete(request)

        assertEquals(200, resp.status)
    }

    @Test
    fun search_success() {
        val request = DocumentSearchRequest(listOf("fake"))

        val resp = service.search(request)

        assertEquals(200, resp.status)
    }
}