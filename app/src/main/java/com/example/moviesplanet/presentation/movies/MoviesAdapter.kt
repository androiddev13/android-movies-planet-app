package com.example.moviesplanet.presentation.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.R
import com.example.moviesplanet.data.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.*

class MoviesAdapter(private val onClick: (Movie) -> Unit) : PagedListAdapter<Movie, MoviesAdapter.MovieViewHolder>(moviesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater =  LayoutInflater.from(parent.context);
        return MovieViewHolder(inflater.inflate(R.layout.item_movie, parent, false));
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(items: PagedList<Movie>) {
        submitList(items)
    }

    inner class MovieViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Movie?) {
            item?.let {
                Picasso.with(containerView.context)
                    .load(item.posterPath)
                    .into(posterImageView)
                posterImageView.setOnClickListener { onClick(item) }
            }
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