package com.example.moviesplanet.presentation.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.R
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.storage.remote.MoviesServiceApi
import com.example.moviesplanet.presentation.generic.BaseViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_favorite_movie.*
import kotlinx.android.synthetic.main.item_movie.*

class MyFavoritesAdapter() : RecyclerView.Adapter<MyFavoritesAdapter.FavoriteViewHolder>() {

    var list = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater =  LayoutInflater.from(parent.context);
        return FavoriteViewHolder(inflater.inflate(R.layout.item_favorite_movie, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setData(items: List<Movie>) {
        list = items
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(override val containerView: View) : BaseViewHolder(containerView), LayoutContainer {

        override fun bind(position: Int) {
            val item = list[position]
            Picasso.with(containerView.context)
                .load(item.posterPath)
                .into(movieImageView)
            movieTextView.text = item.title
        }
    }
}