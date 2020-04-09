package ru.speinmerk.mars_photo.data.source

import androidx.paging.PagedList
import ru.speinmerk.mars_photo.BuildConfig
import ru.speinmerk.mars_photo.data.Photo
import ru.speinmerk.mars_photo.data.source.network.*
import ru.speinmerk.mars_photo.data.source.prefs.SharedPreference
import ru.speinmerk.mars_photo.utils.PagingRequestHelper
import ru.speinmerk.mars_photo.utils.createStatusLiveData
import java.util.concurrent.Executor

class PhotoBoundaryCallback(
    private val ioExecutor: Executor,
    private val photoApiService: PhotoApiService,
    private val handleResponse: (PhotosResponse) -> Unit,
    private val prefs: SharedPreference
) : PagedList.BoundaryCallback<Photo>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            loadPhotos(prefs.lastPageLoaded, it)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Photo) {
        if (prefs.isFullLoaded) return
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            loadPhotos(prefs.lastPageLoaded, it)
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Photo) {
    }

    private fun loadPhotos(
        page: Int,
        callback: PagingRequestHelper.Request.Callback
    ) {
        ioExecutor.execute {
            val result = photoApiService.loadPhotos(
                apiKey = BuildConfig.API_KEY,
                sol = 100,
                page = page
            )
            when (result) {
                is ApiErrorResponse -> {
                    callback.recordFailure(result.error)
                }
                is ApiEmptyResponse -> {
                    prefs.isFullLoaded = true
                    callback.recordSuccess()
                }
                is ApiSuccessResponse -> {
                    handleResponse(result.body)
                    prefs.lastPageLoaded++
                    callback.recordSuccess()
                }
            }
        }
    }
}