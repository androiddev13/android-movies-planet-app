package com.example.moviesplanet.presentation.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviesplanet.R
import com.example.moviesplanet.data.Status
import com.example.moviesplanet.presentation.favorites.MyFavoritesActivity
import com.example.moviesplanet.presentation.generic.LiveDataEventObserver
import com.example.moviesplanet.presentation.moviedetails.MovieDetailsActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.view_error_message.view.*
import javax.inject.Inject

class MoviesActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MoviesViewModel

    private lateinit var actionBarToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        initView()

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel::class.java)

        viewModel.moviesLiveData.observe(this, Observer {
            (mainRecyclerView.adapter as MoviesAdapter).setData(it)
        })

        viewModel.moviesLoadingStatusLiveData.observe(this, Observer {
            when(it.status) {
                Status.FIRST_LOADING -> {
                    moviesProgressBar.visibility = View.VISIBLE
                    errorMessageView.visibility = View.GONE
                }
                Status.FIRST_LOADING_FAILED -> {
                    moviesProgressBar.visibility = View.GONE
                    errorMessageView.visibility = View.VISIBLE
                }
                Status.FIRST_LOADING_SUCCESS -> {
                    moviesProgressBar.visibility = View.GONE
                }
                else -> (mainRecyclerView.adapter as MoviesAdapter).setLoadingStatus(it)
            }
        })

        viewModel.moviesNavigationLiveData.observe(this, LiveDataEventObserver {
            when (it.navigation) {
                Navigation.MOVIE_DETAILS -> startActivity(MovieDetailsActivity.getIntent(this, it.movie))
                Navigation.MY_FAVORITES -> startActivity(MyFavoritesActivity.getIntent(this))
            }
        })
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mainRecyclerView.layoutManager = manager
        mainRecyclerView.adapter = MoviesAdapter({ movie -> viewModel.onMovieClick(movie) }, { viewModel.tryAgainClick() })

        errorMessageView.tryAgainButton.setOnClickListener { viewModel.tryAgainClick() }

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuFavorites -> viewModel.onMyFavoritesClick()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == R.id.optionPopular -> {
                viewModel.sortByPopularClick()
                true
            }
            item?.itemId == R.id.optionRate -> {
                viewModel.sortByTopRatedClick()
                true
            }
            item?.itemId == R.id.optionUpcoming -> {
                viewModel.sortByUpcomingClick()
                true
            }
            actionBarToggle.onOptionsItemSelected(item) -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
