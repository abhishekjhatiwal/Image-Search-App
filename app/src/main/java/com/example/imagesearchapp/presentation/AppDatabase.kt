package com.example.imagesearchapp.presentation

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.imagesearchapp.data.local.ImageDao
import com.example.imagesearchapp.data.local.RemoteKeyDao
import com.example.imagesearchapp.model.local.ImageEntity
import com.example.imagesearchapp.model.local.RemoteKey

@Database(entities = [ImageEntity::class, RemoteKey::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "app.db").build()
    }

    abstract fun getImageDao(): ImageDao

    abstract fun getRemoteKeyDao(): RemoteKeyDao
}