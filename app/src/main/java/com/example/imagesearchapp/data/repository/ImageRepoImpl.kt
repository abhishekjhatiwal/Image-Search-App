package com.example.imagesearchapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.imagesearchapp.data.ApiService
import com.example.imagesearchapp.data.local.ImageDao
import com.example.imagesearchapp.data.local.RemoteKeyDao
import com.example.imagesearchapp.data.maper.ImageDTOtoImageMapper
import com.example.imagesearchapp.data.maper.ImageEntityToImageMapper
import com.example.imagesearchapp.data.pagingsource.ImagePagingSource
import com.example.imagesearchapp.data.pagingsource.ImageRemoteMediator
import com.example.imagesearchapp.domain.Image
import com.example.imagesearchapp.repository.ImageRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImageRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: ImageDTOtoImageMapper,
    private val imageDao: ImageDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val imageEntityToImageMapper: ImageEntityToImageMapper
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

    @OptIn(ExperimentalPagingApi::class)
    override fun getRemoteMediatorImages(q: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                imageDao.getImages(q)
            },
            remoteMediator = ImageRemoteMediator(
                query = q,
                imageDao = imageDao,
                remoteKeyDao = remoteKeyDao,
                apiService = apiService
            )
        ).flow.map { it ->
            it.map {
                imageEntityToImageMapper.map(it)
            }
        }
    }
}
