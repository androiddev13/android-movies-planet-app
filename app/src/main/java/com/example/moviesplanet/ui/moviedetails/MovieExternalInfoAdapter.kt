package com.example.moviesplanet.ui.moviedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.R
import com.example.data.model.MovieExternalInfo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie_external_info.*

class MovieExternalInfoAdapter(private val onClick: (MovieExternalInfo) -> Unit) : RecyclerView.Adapter<MovieExternalInfoAdapter.ExternalInfoViewHolder>() {

    var list = listOf<MovieExternalInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExternalInfoViewHolder {
        val inflater =  LayoutInflater.from(parent.context);
        return ExternalInfoViewHolder(inflater.inflate(R.layout.item_movie_external_info, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ExternalInfoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setData(items: List<MovieExternalInfo>) {
        list = items
        notifyDataSetChanged()
    }

    inner class ExternalInfoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

       fun bind(item: MovieExternalInfo) {
            infoTextView.text = item.name
            showImageButton.setOnClickListener { onClick(item) }
            externalInfoContainer.setOnClickListener { onClick(item) }
       }
    }
}