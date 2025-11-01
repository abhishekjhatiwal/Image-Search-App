package com.example.imagesearchapp.data.remote

data class ImageResponse(
    val hits: List<ImageDTO>,
    val total: Int,
    val totalHits: Int
)