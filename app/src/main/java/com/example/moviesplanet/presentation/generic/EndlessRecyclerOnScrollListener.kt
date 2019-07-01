package com.example.moviesplanet.presentation.generic

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener() {

    private var previousTotal = 0

    private var loading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.layoutManager!!.itemCount
        val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        val visibleThreshold = 6
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            // End has been reached.
            onLoadMore()
            loading = true
        }
    }

    fun reset() {
        previousTotal = 0
        loading = true
    }

    fun loadFinished() {
        loading = false
    }

    abstract fun onLoadMore()

}