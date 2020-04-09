package ru.speinmerk.mars_photo.data.source.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.speinmerk.mars_photo.data.Photo

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(photos: List<Photo>)

    @Query("SELECT * FROM photos WHERE isDeleted = 0 ORDER BY id ASC")
    fun getPaged(): DataSource.Factory<Int, Photo>

    @Query("SELECT * FROM photos WHERE isDeleted = 0 ORDER BY id ASC")
    suspend fun getAll(): List<Photo>

//    fun delete(ids: List<Int>)

}