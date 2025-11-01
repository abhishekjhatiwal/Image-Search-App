package com.example.imagesearchapp.data.maper

import com.example.imagesearchapp.data.remote.ImageDTO
import com.example.imagesearchapp.model.local.ImageEntity

class ImageDTOtoImageEntityMapper(
    private val query: String
) : Mapper<ImageDTO, ImageEntity> {
    override fun map(from: ImageDTO): ImageEntity {
        return ImageEntity(
            id = from.id.toString(),
            imageUrl = from.largeImageURL,
            query = query
        )
    }
}