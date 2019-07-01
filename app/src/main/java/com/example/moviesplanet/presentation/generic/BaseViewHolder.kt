package com.example.moviesplanet.presentation.generic

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView){

    abstract fun bind(position: Int)

}