package com.example.githubsearchapp.data

data class User(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)