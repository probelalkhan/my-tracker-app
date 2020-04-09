package net.simplifiedcoding.mytracker.ui.home

import android.view.View

interface RecyclerViewClickListener<T : Any> {
    fun onRecyclerViewClick(view: View, item: T)
}