package com.example.imagesearchapp.data.maper

import com.example.imagesearchapp.data.remote.ImageDTO
import com.example.imagesearchapp.domain.Image
import jakarta.inject.Inject

class ImageDTOtoImageMapper @Inject constructor() : Mapper<ImageDTO, Image> {
    override fun map(from: ImageDTO): Image {
        return Image(
            id = from.id.toString(),
            imageUri = from.largeImageURL,
            uuid = from.user_id.toString()
        )
    }
}