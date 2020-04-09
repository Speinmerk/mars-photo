package ru.speinmerk.mars_photo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get
import ru.speinmerk.mars_photo.R
import java.util.*

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
            if(it.isNotEmpty()) {
                swipeLayout.isEnabled = false
            }
            adapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.isLoading.observe(viewLifecycleOwner, Observer {
            swipeLayout.isRefreshing = it
        })
        presenter.isRefreshing.observe(viewLifecycleOwner, Observer {
            swipeLayout.isRefreshing = it
        })
        swipeLayout.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun showImage(list: List<String>, position: Int) {
        ImageViewer.Builder(context, list)
            .setStartPosition(position)
            .hideStatusBar(false)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .show()
    }

    override fun showContextMenu(menuItems: ArrayList<Pair<String, () -> Unit>>) {
        context ?: return
        val actionList = menuItems.map { it.first }.toTypedArray()
        AlertDialog.Builder(context!!).setItems(actionList) { _, which ->
            menuItems[which].second()
        }.create().show()
    }

    override fun showError(message: String, button: String?, btnCallback: (() -> Unit)?) {
        val snackBar = Snackbar.make(view ?: return, message, Snackbar.LENGTH_LONG)
        if (button != null && btnCallback != null) {
            snackBar.setAction(button) {
                btnCallback()
            }
        }
        snackBar.show()
    }

}
