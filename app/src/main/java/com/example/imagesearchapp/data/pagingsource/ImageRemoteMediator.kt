package com.example.imagesearchapp.data.pagingsource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.imagesearchapp.data.ApiService
import com.example.imagesearchapp.data.local.ImageDao
import com.example.imagesearchapp.data.local.RemoteKeyDao
import com.example.imagesearchapp.data.maper.ImageDTOtoImageEntityMapper
import com.example.imagesearchapp.data.maper.mapAll
import com.example.imagesearchapp.model.local.ImageEntity
import com.example.imagesearchapp.model.local.RemoteKey

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val query: String,
    private val imageDao: ImageDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val apiService: ApiService
) : RemoteMediator<Int, ImageEntity>() {
    val mapper = ImageDTOtoImageEntityMapper(query)

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = state.anchorPosition?.let {
                    state.closestItemToPosition(it)?.let {
                        remoteKeyDao.getRemoteKey(it.id)
                    }
                }
                remoteKey?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKey = state.pages?.firstOrNull {
                    it.data.isNotEmpty()
                }?.data?.firstOrNull()?.let {
                    remoteKeyDao.getRemoteKey(it.id)
                }
                remoteKey?.prevKey ?: return MediatorResult.Success(remoteKey != null)
            }

            LoadType.APPEND -> {
                val remoteKey = state.pages?.lastOrNull {
                    it.data.isNotEmpty()
                }?.data?.lastOrNull()?.let {
                    remoteKeyDao.getRemoteKey(it.id)
                }
                remoteKey?.nextKey ?: return MediatorResult.Success(remoteKey != null)
            }
        }
        return try {
            val response = apiService.getImages(q = query, page = page)
            val remoteImages = response.hits.distinctBy { it.id }
            val endOfPaginationReached = remoteImages.size < state.config.pageSize
            val prevPage = if (page > 1) page - 1 else null
            val nextPage = if (endOfPaginationReached) null else page + 1

            val remoteKeys = remoteImages.map {
                RemoteKey(
                    id = it.id.toString(),
                    prevKey = prevPage,
                    nextKey = nextPage,
                    query = query
                )
            }
            remoteKeyDao.insertAll(remoteKeys)
            imageDao.insertAll(mapper.mapAll(remoteImages))
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}