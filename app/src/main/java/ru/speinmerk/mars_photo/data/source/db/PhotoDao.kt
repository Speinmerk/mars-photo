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

    @Query("SELECT * FROM photos WHERE isDeleted = 0")
    fun getPaged(): DataSource.Factory<Int, Photo>

    @Query("SELECT * FROM photos WHERE isDeleted = 0")
    suspend fun getAll(): List<Photo>

    @Query("UPDATE photos SET isDeleted = 1 WHERE id = :id ")
    suspend fun delete(id: Int)

}