package com.example.imagesearchapp.repository

import androidx.paging.Pager
import com.example.imagesearchapp.domain.Image

interface ImageRepository {
    fun getImages(q: String) : Pager<Int, Image>
}