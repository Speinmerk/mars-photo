package ru.speinmerk.mars_photo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.map
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.speinmerk.mars_photo.App
import ru.speinmerk.mars_photo.R
import ru.speinmerk.mars_photo.data.Photo
import ru.speinmerk.mars_photo.data.source.NetworkState
import ru.speinmerk.mars_photo.data.source.PhotoRepository
import ru.speinmerk.mars_photo.data.source.Status

@InjectViewState
class MainPresenter(
    private val repository: PhotoRepository
) : MvpPresenter<MainView>(), CoroutineScope by MainScope() {
    private val listing = repository.getListingPhotos()
    val photos = listing.pagedList
    val isLoading = listing.networkState.map {
        handleNetworkState(it, ::retry)
    }
    val isRefreshing = listing.refreshState.map {
        handleNetworkState(it, ::refresh)
    }

    private fun handleNetworkState(state: NetworkState, retry: () -> Unit): Boolean {
        return when (state.status) {
            Status.SUCCESS -> false
            Status.RUNNING -> true
            Status.FAILED -> {
                viewState.showError(
                    message = App.context.getString(R.string.error_loading),
                    button = App.context.getString(R.string.btn_retry),
                    btnCallback = ::retry
                )
                false
            }
        }
    }

    fun refresh() {
        listing.refresh.invoke()
    }

    private fun retry() {
        listing.retry.invoke()
    }

    fun onClick(photo: Photo?) {
        photo ?: return
        launch(Dispatchers.Default) {
            val photos = repository.getPhotos()
            val position = photos.indexOfFirst { it.id == photo.id }
            val urls = photos.map { it.src }
            withContext(Dispatchers.Main) {
                viewState.showImage(urls, position)
            }
        }
    }

    fun onLongClick(photo: Photo?): Boolean {
        photo ?: return false
        val menuItems = ArrayList<Pair<String, () -> Unit>>()
        val btnDeleteText = App.context.getString(R.string.delete)
        menuItems.add(btnDeleteText to {
            delete(photo)
        })
        viewState.showContextMenu(menuItems)
        return true
    }

    private fun delete(photo: Photo) {
        launch {
            repository.deletePhoto(photo.id)
        }
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }

}
