package com.example.imagesearchapp.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagesearchapp.data.ApiService
import com.example.imagesearchapp.data.api_key
import com.example.imagesearchapp.data.maper.ImageDTOtoImageMapper
import com.example.imagesearchapp.data.maper.mapAll
import com.example.imagesearchapp.domain.Image

class ImagePagingSource(
    private val apiService: ApiService,
    private val q: String,
    private val mapper: ImageDTOtoImageMapper
) : PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: 1
        val pageSize = params.loadSize

        return try {
//            delay(2000)
            val images = apiService.getImages(api_key, q, page = page)
            LoadResult.Page(
                data = mapper.mapAll(images.hits),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (images.hits.size < pageSize) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}