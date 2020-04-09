package ru.speinmerk.mars_photo.data.source

import ru.speinmerk.mars_photo.data.Photo

interface PhotoRepository {
    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }

    fun getListingPhotos(pageSize: Int = DEFAULT_PAGE_SIZE): Listing<Photo>
    suspend fun getPhotos(): List<Photo>

}