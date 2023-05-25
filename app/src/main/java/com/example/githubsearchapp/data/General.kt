package com.example.githubsearchapp.data

data class General(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)
