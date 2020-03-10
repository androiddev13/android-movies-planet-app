package com.example.moviesplanet.presentation.favorites

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class MyFavoritesActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MyFavoritesActivity::class.java)
        }
    }

}