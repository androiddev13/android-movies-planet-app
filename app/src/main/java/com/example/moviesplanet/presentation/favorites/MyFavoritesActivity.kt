package com.example.moviesplanet.presentation.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.R
import com.example.moviesplanet.presentation.MovieDetailsNavigation
import com.example.moviesplanet.presentation.generic.LiveDataEventObserver
import com.example.moviesplanet.presentation.generic.VerticalDividerItemDecoration
import com.example.moviesplanet.presentation.moviedetails.MovieDetailsActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_my_favorites.*
import javax.inject.Inject

class MyFavoritesActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MyFavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorites)

        initView()

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyFavoritesViewModel::class.java)

        viewModel.favoriteMoviesLiveData.observe(this, Observer {
            (moviesRecyclerView.adapter as MyFavoritesAdapter).setData(it)
            val noFavoritesViewVisibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            noFavoritesTextView.visibility = noFavoritesViewVisibility
        })

        viewModel.favoritesNavigationLiveData.observe(this, LiveDataEventObserver {
            when (it) {
                is MovieDetailsNavigation -> startActivity(MovieDetailsActivity.getIntent(this, it.movie))
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.title_my_favorite_movies)

        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        moviesRecyclerView.apply {
            layoutManager = manager
            addItemDecoration(VerticalDividerItemDecoration(context))
            adapter = MyFavoritesAdapter {
                viewModel.onMovieClick(it)
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MyFavoritesActivity::class.java)
        }
    }
}