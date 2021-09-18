package org.bloglog.processor.model

data class Document (
    val id: String,
    val title: String,
    val repo: String,
    val branch: String,
    val path: String,
)