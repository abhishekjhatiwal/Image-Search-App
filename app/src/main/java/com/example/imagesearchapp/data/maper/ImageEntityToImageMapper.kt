package com.example.imagesearchapp.data.maper

import com.example.imagesearchapp.domain.Image
import com.example.imagesearchapp.model.local.ImageEntity
import jakarta.inject.Inject
import java.util.UUID

class ImageEntityToImageMapper @Inject constructor() : Mapper<ImageEntity, Image> {
    override fun map(from: ImageEntity): Image {
        return Image(
            id = from.id,
            imageUri = from.imageUrl,
            uuid = UUID.randomUUID().toString()
        )
    }
}