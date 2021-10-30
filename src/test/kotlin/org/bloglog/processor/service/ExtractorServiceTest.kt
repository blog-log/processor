package org.bloglog.processor.service

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ExtractorServiceTest {

    @Test
    fun get() {
        val service = ExtractorService("https://development.bloglog.io/api/v1/extractor")
        val resp = service.extract(ExtractRequest("https://github.com/brandoncate-personal/blog-content"))

        assertEquals(null, resp.error)
    }
}