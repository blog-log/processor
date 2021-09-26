package org.bloglog.processor.service

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class SearchManagerServiceTest {
    private lateinit var service: SearchManagerService

    @BeforeEach
    fun setUp() {
        service = SearchManagerService("https://development.bloglog.io/api/v1/searcher")
    }

    @Test
    fun set() {
        val request = SearchSetRequest(
            "fake-postman1",
            "fake",
            "https://github.com/brandoncate-personal/blog-content",
            "main",
            "src/test2.md"
        )

        val resp = service.set(request)

        assertEquals("fake-postman1", resp.id)
    }

    @Test
    fun delete() {
        val request = SearchDeleteRequest(
            "fake-postman1",
        )

        val resp = service.delete(request)

        assertEquals("fake-postman1", resp.id)
    }
}