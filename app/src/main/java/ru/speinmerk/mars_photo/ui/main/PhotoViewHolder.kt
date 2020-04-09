package ru.speinmerk.mars_photo.ui.main

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.facebook.drawee.view.SimpleDraweeView
import ru.speinmerk.mars_photo.R
import ru.speinmerk.mars_photo.data.Photo

class PhotoViewHolder(
    private val imageView: SimpleDraweeView,
    private val onClick: (Photo?) -> Unit,
    private val onLongClick: (Photo?) -> Boolean
) : RecyclerView.ViewHolder(imageView) {

    init {
        imageView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(1, 1, 1, 1) }
        imageView.aspectRatio = 1f
        imageView.hierarchy.setPlaceholderImage(R.drawable.placeholder)
        imageView.hierarchy.setProgressBarImage(ProgressBarDrawable())
    }

    fun bindTo(photo: Photo?) {
        imageView.setImageURI(photo?.src)
        imageView.setOnClickListener { onClick(photo) }
        imageView.setOnLongClickListener { onLongClick(photo) }
    }

}