package com.example.moviesplanet.presentation.moviedetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.BuildConfig
import com.example.moviesplanet.R
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.presentation.generic.LiveDataEventObserver
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_movie_details.*
import javax.inject.Inject

class MovieDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        initView()

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel::class.java)
        viewModel.setMovie(getMovie())

        viewModel.movieDetailsLiveData.observe(this, Observer {
            Picasso.with(this)
                .load(BuildConfig.MOVIES_IMAGE_BASE_URL + it.movie.posterPath)
                .into(infoPosterImageView)
            title = it.movie.title
            infoYearTextView.text = it.movie.releaseDate
            infoRateTextView.text = getString(R.string.rate_format, it.movie.voteAverage)
            descriptionTextView.text = it.movie.overview

            val favImageViewSrc = if (it.isFavorite) R.drawable.ic_favorite_white else R.drawable.ic_favorite_border_white
            favImageView.setImageResource(favImageViewSrc)

            val visibility = if (it.externalInfo.isEmpty()) View.GONE else View.VISIBLE
            (infoRecyclerView.adapter as MovieExternalInfoAdapter).setData(it.externalInfo)
            infoRecyclerView.visibility = visibility

            containerDetail.visibility = View.VISIBLE
        })

        viewModel.navigateToInfoViewLiveData.observe(this, LiveDataEventObserver {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        })

        viewModel.loadingIndicatorLiveData.observe(this, Observer {
            val visibility = if (it) View.VISIBLE else View.GONE
            progressBar.visibility = visibility
        })

        viewModel.loadFailedLiveData.observe(this, Observer {
            val visibility = if (it) View.VISIBLE else View.GONE
            badRequestContainer.visibility = visibility
        })

        viewModel.favoriteLoadingIndicatorLiveData.observe(this, Observer {
            if (it) {
                favProgressBar.visibility = View.VISIBLE
                favImageView.setImageResource(0)
            } else {
                favProgressBar.visibility = View.GONE
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

        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        infoRecyclerView.layoutManager = manager
        infoRecyclerView.isNestedScrollingEnabled = false
        infoRecyclerView.adapter = MovieExternalInfoAdapter { movieExternalInfo -> viewModel.onExternalInfoClick(movieExternalInfo)  }

        tryAgainButton.setOnClickListener { viewModel.onTryAgainClick() }

        favImageView.setOnClickListener { viewModel.toggleFavMovie() }
    }

    private fun getMovie() = intent.getParcelableExtra<Movie>(KEY_MOVIE)

    companion object {

        private val KEY_MOVIE = MovieDetailsActivity::class.java.simpleName + ".Movie"

        fun getIntent(context: Context, movie: Movie): Intent {
            return Intent(context, MovieDetailsActivity::class.java).putExtra(KEY_MOVIE, movie)
        }
    }
}