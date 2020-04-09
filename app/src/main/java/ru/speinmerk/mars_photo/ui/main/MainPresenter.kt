package ru.speinmerk.mars_photo.ui.main

import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.speinmerk.mars_photo.App
import ru.speinmerk.mars_photo.R
import ru.speinmerk.mars_photo.data.Photo
import ru.speinmerk.mars_photo.data.source.PhotoRepository

@InjectViewState
class MainPresenter(
    private val repository: PhotoRepository
) : MvpPresenter<MainView>(), CoroutineScope by MainScope() {
    private val listing = repository.getListingPhotos()
    val photos = listing.pagedList
    val networkState = listing.networkState
    val refreshState = listing.refreshState

    fun refresh() {
        listing.refresh.invoke()
    }

    fun retry() {
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
