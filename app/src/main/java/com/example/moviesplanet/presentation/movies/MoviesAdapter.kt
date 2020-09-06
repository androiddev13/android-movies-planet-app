package com.example.moviesplanet.presentation.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviesplanet.R
import com.example.data.model.LoadingStatus
import com.example.data.model.Status
import com.example.data.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.*
import kotlinx.android.synthetic.main.item_paging_loading_status.view.*
import kotlinx.android.synthetic.main.view_error_message.view.*

class MoviesAdapter(private val onClick: (Movie) -> Unit,
                    private val onRetryClick: () -> Unit) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(moviesDiffCallback) {

    private enum class Item(val value: Int) {
        MOVIE(0), LOADING_STATUS(1)
    }

    private var loadingStatus: LoadingStatus? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater =  LayoutInflater.from(parent.context)
        return when (viewType) {
            Item.MOVIE.value -> MovieViewHolder(inflater.inflate(R.layout.item_movie, parent, false))
            Item.LOADING_STATUS.value -> MovieLoadingStatusViewHolder((inflater.inflate(R.layout.item_paging_loading_status, parent, false)))
            else -> throw Exception("Unknown view type.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            Item.MOVIE.value -> (holder as MovieViewHolder).bind(getItem(position))
            Item.LOADING_STATUS.value -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                (holder as MovieLoadingStatusViewHolder).bind(loadingStatus)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) Item.LOADING_STATUS.value else Item.MOVIE.value
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return loadingStatus != null && loadingStatus != LoadingStatus.LOADING_SUCCESS
    }

    fun setData(items: PagedList<Movie>) {
        submitList(items)
    }

    fun setLoadingStatus(newLoadingStatus: LoadingStatus) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = loadingStatus
                val hadExtraRow = hasExtraRow()
                loadingStatus = newLoadingStatus
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newLoadingStatus) {
                    notifyItemChanged(itemCount - 1)
                }
            } else {
                notifyDataSetChanged()
            }
        }
    }

    inner class MovieViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Movie?) {
            item?.let {
                Picasso.with(containerView.context)
                    .load(item.posterPath)
                    .fit()
                    .noFade()
                    .placeholder(R.drawable.placeholder_movie)
                    .error(R.drawable.placeholder_movie)
                    .into(posterImageView)
                posterImageView.setOnClickListener { onClick(item) }
            }
        }
    }

    inner class MovieLoadingStatusViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: LoadingStatus?) {
            itemView.errorMessageTextView.text = item?.message
            itemView.errorMessageView.visibility = if (item?.status == Status.LOADING_FAILED) View.VISIBLE else View.INVISIBLE
            itemView.tryAgainButton.setOnClickListener { onRetryClick() }
            itemView.progressBar.visibility = if (item?.status == Status.LOADING) View.VISIBLE else View.INVISIBLE
        }
    }

    companion object {
        private val moviesDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}