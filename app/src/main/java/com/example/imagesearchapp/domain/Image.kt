package com.example.imagesearchapp.domain

import java.util.UUID

data class Image(
    val id: String,
    val imageUri: String,
    val uuid: String = UUID.randomUUID().toString()
)