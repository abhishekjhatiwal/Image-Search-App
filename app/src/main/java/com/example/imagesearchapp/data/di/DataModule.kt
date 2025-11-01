package com.example.imagesearchapp.data.di

import android.content.Context
import com.example.imagesearchapp.data.ApiService
import com.example.imagesearchapp.data.local.ImageDao
import com.example.imagesearchapp.data.local.RemoteKeyDao
import com.example.imagesearchapp.data.maper.ImageDTOtoImageMapper
import com.example.imagesearchapp.data.maper.ImageEntityToImageMapper
import com.example.imagesearchapp.data.repository.ImageRepoImpl
import com.example.imagesearchapp.presentation.AppDatabase
import com.example.imagesearchapp.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
        mapper: ImageDTOtoImageMapper,
        imageDao: ImageDao,
        remoteKeyDao: RemoteKeyDao,
        imageEntityToImageMapper: ImageEntityToImageMapper
    ): ImageRepository {
        return ImageRepoImpl(
            apiService, mapper,
            imageDao = imageDao,
            remoteKeyDao = remoteKeyDao,
            imageEntityToImageMapper = imageEntityToImageMapper
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(context = appContext)
    }

    @Provides
    fun provideImageDao(appDatabase: AppDatabase): ImageDao {
        return appDatabase.getImageDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeyDao(appDatabase: AppDatabase): RemoteKeyDao {
        return appDatabase.getRemoteKeyDao()
    }

}