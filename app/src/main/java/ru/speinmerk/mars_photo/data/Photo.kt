package ru.speinmerk.mars_photo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey
    val id: Int,
    val src: String,
    var isDeleted: Boolean
)