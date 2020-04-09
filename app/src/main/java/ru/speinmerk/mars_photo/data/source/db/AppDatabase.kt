package ru.speinmerk.mars_photo.data.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.speinmerk.mars_photo.data.Photo

@Database(entities = [Photo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photos(): PhotoDao
}