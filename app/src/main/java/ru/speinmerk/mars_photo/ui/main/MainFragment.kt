package ru.speinmerk.mars_photo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.main_fragment.view.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get
import ru.speinmerk.mars_photo.R

class MainFragment : MvpAppCompatFragment(), MainView {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val presenter by moxyPresenter { get<MainPresenter>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        initRecyclerView(view.recycler_view)
        return view
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        val adapter = PhotoAdapter(presenter::onClick, presenter::onLongClick)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        recyclerView.adapter = adapter
        presenter.photos.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun showImage(list: List<String>, position: Int) {
        ImageViewer.Builder(context, list)
            .setStartPosition(position)
            .hideStatusBar(false)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
//            .setBackgroundColorRes(colorRes) //.setBackgroundColor(color)
//            .setImageMargin(margin) //.setImageMarginPx(marginPx)
//            .setContainerPadding(
//                this,
//                dimen
//            ) //.setContainerPadding(this, dimenStart, dimenTop, dimenEnd, dimenBottom)
            //.setContainerPaddingPx(padding)
            //.setContainerPaddingPx(start, top, end, bottom)
//            .setCustomImageRequestBuilder(imageRequestBuilder)
//            .setCustomDraweeHierarchyBuilder(draweeHierarchyBuilder)
//            .setImageChangeListener(imageChangeListener)
//            .setOnDismissListener(onDismissListener)
//            .setOverlayView(overlayView)
            .show()
    }

}