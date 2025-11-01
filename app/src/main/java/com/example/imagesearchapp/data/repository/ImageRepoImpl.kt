package com.example.imagesearchapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.imagesearchapp.data.ApiService
import com.example.imagesearchapp.data.maper.ImageDTOtoImageMapper
import com.example.imagesearchapp.data.pagingsource.ImagePagingSource
import com.example.imagesearchapp.domain.Image
import com.example.imagesearchapp.repository.ImageRepository
import jakarta.inject.Inject

class ImageRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: ImageDTOtoImageMapper
) : ImageRepository {
    override fun getImages(q: String): Pager<Int, Image> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = false,
                initialLoadSize = 10,
//                maxSize = 100
            ),
            pagingSourceFactory = {
                ImagePagingSource(
                    apiService = apiService,
                    q = q,
                    mapper = mapper
                )
            }
        )
    }
}