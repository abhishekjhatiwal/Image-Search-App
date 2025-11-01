package com.example.imagesearchapp.model.usecase

import com.example.imagesearchapp.repository.ImageRepository
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    operator fun invoke(q: String) = imageRepository.getImages(q)
}