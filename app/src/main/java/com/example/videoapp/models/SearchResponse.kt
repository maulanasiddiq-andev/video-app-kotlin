package com.example.videoapp.models

data class SearchResponse<T>(
    val items: List<T>,
    val totalItems: Int,
    val currentPage: Int,
    val totalPages: Int,
    val pageSize: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)