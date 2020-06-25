package com.example.moviesplanet.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieGenre(val id: Int,
                      val name: String) : Parcelable