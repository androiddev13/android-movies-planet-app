package com.example.moviesplanet.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.R
import com.example.data.model.Movie
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_favorite_movie.*

class MyFavoritesAdapter(private val onClick: (Movie) -> Unit) : RecyclerView.Adapter<MyFavoritesAdapter.FavoriteViewHolder>() {

    var list = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater =  LayoutInflater.from(parent.context);
        return FavoriteViewHolder(inflater.inflate(R.layout.item_favorite_movie, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setData(items: List<Movie>) {
        val diff = DiffUtil.calculateDiff(FavoritesDiffCallback(items, list))
        list = items
        diff.dispatchUpdatesTo(this)
    }

    inner class FavoriteViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Movie) {
            titleTextView.text = item.title
            subtitleTextView.text = item.overview
            containerView.setOnClickListener { onClick(item) }
        }
    }

    inner class FavoritesDiffCallback(private val new: List<Movie>,
                                      private val old: List<Movie>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition].id == new[newItemPosition].id

        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == new[newItemPosition]
    }
}