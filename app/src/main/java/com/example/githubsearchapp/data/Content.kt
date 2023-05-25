package com.example.githubsearchapp.data

data class Content(
    val _links: Links,
    val download_url: String,
    val entries: List<Entry>,
    val git_url: String,
    val html_url: String,
    val name: String,
    val path: String,
    val sha: String,
    val size: Int,
    val type: String,
    val url: String
)