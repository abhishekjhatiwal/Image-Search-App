package com.example.imagesearchapp.model.usecase

import com.example.imagesearchapp.repository.ImageRepository
import javax.inject.Inject

class GetImagesFromRemoteMediator @Inject constructor(
    private val imageRepository: ImageRepository
) {
     /* override*/ fun invoke(q: String) = imageRepository.getRemoteMediatorImages(q)
}