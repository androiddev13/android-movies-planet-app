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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesplanet.R
import com.example.moviesplanet.presentation.favorites.MyFavoritesActivity
import com.example.moviesplanet.presentation.generic.LiveDataEventObserver
import com.example.moviesplanet.presentation.moviedetails.MovieDetailsActivity
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
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


        // TODO revowk/remove
        viewModel.firstLoadFailedLiveData.observe(this, Observer {
            val visibility = if (it) View.VISIBLE else View.GONE
            badRequestContainer.visibility = visibility
        })

        viewModel.loadFailedLiveData.observe(this, LiveDataEventObserver {
            val message = it ?: getString(R.string.message_error_generic)
            Snackbar.make(mainContainer, message, Snackbar.LENGTH_SHORT).show()
        })

        viewModel.loadingIndicatorLiveData.observe(this, Observer {
            val visibility = if (it) View.VISIBLE else View.GONE
            moviesProgressBar.visibility = visibility
        })

        viewModel.navigateToDetailsLiveData.observe(this, LiveDataEventObserver {
            startActivity(MovieDetailsActivity.getIntent(this, it))
        })

        viewModel.navigateToMyFavoritesLiveData.observe(this, LiveDataEventObserver {
            startActivity(MyFavoritesActivity.getIntent(this))
        })
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val manager = GridLayoutManager(this, 2)
        mainRecyclerView.layoutManager = manager
        mainRecyclerView.adapter = MoviesAdapter { movie -> viewModel.onMovieClick(movie) }

        tryAgainButton.setOnClickListener { viewModel.tryAgainClick() }

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
            actionBarToggle.onOptionsItemSelected(item) -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
