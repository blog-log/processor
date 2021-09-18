package org.bloglog.processor.model

data class Repo (
    val id: String,
    val warning: List<String>,
    val error: List<String>,
)