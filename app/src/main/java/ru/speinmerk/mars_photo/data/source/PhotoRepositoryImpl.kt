package ru.speinmerk.mars_photo.data.source

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.toLiveData
import ru.speinmerk.mars_photo.AppExecutors
import ru.speinmerk.mars_photo.BuildConfig
import ru.speinmerk.mars_photo.data.Photo
import ru.speinmerk.mars_photo.data.source.db.AppDatabase
import ru.speinmerk.mars_photo.data.source.network.*
import ru.speinmerk.mars_photo.data.source.prefs.SharedPreference

class PhotoRepositoryImpl(
    private val appExecutors: AppExecutors,
    private val photoApiService: PhotoApiService,
    private val db: AppDatabase,
    private val prefs: SharedPreference
) : PhotoRepository {

    @MainThread
    override fun getListingPhotos(pageSize: Int): Listing<Photo> {
        val boundaryCallback =
            PhotoBoundaryCallback(
                ioExecutor = appExecutors.diskIO(),
                photoApiService = photoApiService,
                handleResponse = this::insertResultIntoDb,
                prefs = prefs
            )
        val livePagedList = db.photos().getPaged().toLiveData(
            pageSize = pageSize,
            boundaryCallback = boundaryCallback
        )
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = refreshTrigger.switchMap {
            refresh()
        }
        return Listing(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    override suspend fun getPhotos(): List<Photo> {
        return db.photos().getAll()
    }

    override suspend fun deletePhoto(id: Int) {
        db.photos().delete(id)
    }

    @MainThread
    private fun refresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        appExecutors.diskIO().execute {
            val result = photoApiService.loadPhotos(
                apiKey = BuildConfig.API_KEY,
                sol = 0,
                page = 0
            )
            when(result) {
                is ApiErrorResponse -> networkState.value = NetworkState.error(result.errorMessage)
                is ApiEmptyResponse -> {
                    networkState.postValue(NetworkState.LOADED)
                }
                is ApiSuccessResponse -> {
                    insertResultIntoDb(result.body)
                    networkState.postValue(NetworkState.LOADED)
                }
            }
        }
        return networkState
    }

    private fun insertResultIntoDb(response: PhotosResponse) {
        val photos = response.photos.map {
            Photo(it.id, it.src, false)
        }
        db.photos().insert(photos)
    }



}