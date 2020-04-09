package ru.speinmerk.mars_photo.data.source.network

import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    val photos: List<Item>
) {
    data class Item(
        val id: Int,
        @SerializedName("img_src")
        val src: String
    )
}