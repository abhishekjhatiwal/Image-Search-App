package com.example.imagesearchapp.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.imagesearchapp.domain.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImages(q: String): Pager<Int, Image>

    fun getRemoteMediatorImages(q: String): Flow<PagingData<Image>>
}