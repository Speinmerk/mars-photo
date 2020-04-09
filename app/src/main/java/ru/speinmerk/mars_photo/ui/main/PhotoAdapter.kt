package ru.speinmerk.mars_photo.ui.main

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.facebook.drawee.view.SimpleDraweeView
import ru.speinmerk.mars_photo.data.Photo

class PhotoAdapter(
    private val onClick: (Photo?) -> Unit,
    private val onLongClick: (Photo?) -> Boolean
) : PagedListAdapter<Photo, PhotoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = SimpleDraweeView(parent.context)
        return PhotoViewHolder(view, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bindTo(photo)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.src == newItem.src
            }

        }
    }
}