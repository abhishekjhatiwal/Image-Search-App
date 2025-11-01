package com.example.imagesearchapp.data.di

import com.example.imagesearchapp.data.ApiService
import com.example.imagesearchapp.data.maper.ImageDTOtoImageMapper
import com.example.imagesearchapp.data.repository.ImageRepoImpl
import com.example.imagesearchapp.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pixabay.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideImageRepository(
        apiService: ApiService,
        mapper: ImageDTOtoImageMapper
    ): ImageRepository {
        return ImageRepoImpl(apiService, mapper)
    }
}