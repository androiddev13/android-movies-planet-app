package com.example.moviesplanet.presentation.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesplanet.R
import com.example.moviesplanet.presentation.generic.EndlessRecyclerOnScrollListener
import com.example.moviesplanet.presentation.generic.LiveDataEventObserver
import com.example.moviesplanet.presentation.moviedetails.MovieDetailsActivity
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MoviesActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel::class.java)

        viewModel.moviesLiveData.observe(this, Observer {
            (mainRecyclerView.adapter as MoviesAdapter).setData(it)
            if (it.isEmpty()) {
                endlessListener.reset()
            }
        })

        viewModel.firstLoadFailedLiveData.observe(this, Observer {
            val visibility = if (it) View.VISIBLE else View.GONE
            badRequestContainer.visibility = visibility
        })

        viewModel.loadFailedLiveData.observe(this, LiveDataEventObserver {
            val message = it ?: getString(R.string.message_error_generic)
            Snackbar.make(mainContainer, message, Snackbar.LENGTH_SHORT).show()
            endlessListener.loadFinished()
        })

        viewModel.loadingIndicatorLiveData.observe(this, Observer {
            val visibility = if (it) View.VISIBLE else View.GONE
            moviesProgressBar.visibility = visibility
        })

        viewModel.navigateToDetailsLiveData.observe(this, LiveDataEventObserver {
            startActivity(MovieDetailsActivity.getIntent(this, it))
        })
    }

    private fun initView() {
        val manager = GridLayoutManager(this, 2)
        mainRecyclerView.layoutManager = manager
        mainRecyclerView.adapter = MoviesAdapter { movie -> viewModel.onMovieClick(movie) }
        mainRecyclerView.addOnScrollListener(endlessListener)

        tryAgainButton.setOnClickListener { viewModel.tryAgainClick() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.optionPopular -> {
                viewModel.sortByPopularClick()
                true
            }
            R.id.optionRate -> {
                viewModel.sortByTopRatedClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val endlessListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() {
            viewModel.loadMoreMovies()
        }
    }
}
