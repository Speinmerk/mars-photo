package ru.speinmerk.mars_photo.ui.main

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.util.ArrayList

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {

    fun showImage(list: List<String>, position: Int)

    fun showContextMenu(menuItems: ArrayList<Pair<String, () -> Unit>>)

}