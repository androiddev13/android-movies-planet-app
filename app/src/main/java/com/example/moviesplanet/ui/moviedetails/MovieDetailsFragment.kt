package com.example.moviesplanet.ui.moviedetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.model.Status
import com.example.moviesplanet.R
import com.example.moviesplanet.ui.ExternalWebPageNavigation
import com.example.moviesplanet.ui.generic.LiveDataEventObserver
import com.example.moviesplanet.ui.generic.VerticalDividerItemDecoration
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details_content.*
import kotlinx.android.synthetic.main.view_error_message.view.*

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.setMovie(args.movie)

        viewModel.movieDetailsLiveData.observe(viewLifecycleOwner, Observer {
            Picasso.with(requireContext())
                .load(it.movie.posterPath)
                .into(infoPosterImageView)
            infoYearTextView.text = it.movie.releaseDate
            infoRateTextView.text = getString(R.string.rate_format, it.movie.voteAverage)
            descriptionTextView.text = it.movie.overview
            genresTextView.visibility = if (it.movieGenres.isNotEmpty()) View.VISIBLE else View.GONE
            genresTextView.text = it.movieGenres.joinToString { genre -> genre.name }

            val favImageViewSrc = if (it.isFavorite) R.drawable.ic_favorite_white else R.drawable.ic_favorite_border_white
            favImageView.setImageResource(favImageViewSrc)

            val visibility = if (it.externalInfo.isEmpty()) View.GONE else View.VISIBLE
            (infoRecyclerView.adapter as MovieExternalInfoAdapter).setData(it.externalInfo)
            infoRecyclerView.visibility = visibility

            containerDetail.visibility = View.VISIBLE
        })

        viewModel.navigationLiveData.observe(viewLifecycleOwner, LiveDataEventObserver {
            when (it) {
                is ExternalWebPageNavigation -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
            }
        })

        viewModel.loadingStatusLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    errorMessageView.visibility = View.GONE
                }
                Status.LOADING_SUCCESS -> {
                    progressBar.visibility = View.GONE
                }
                Status.LOADING_FAILED -> {
                    progressBar.visibility = View.GONE
                    errorMessageView.apply {
                        errorMessageTextView.text = it.message
                        visibility = View.VISIBLE
                    }
                }
                else -> {}
            }
        })

        viewModel.favoriteLoadingIndicatorLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                favProgressBar.visibility = View.VISIBLE
                favImageView.setImageResource(0)
            } else {
                favProgressBar.visibility = View.GONE
            }
        })
    }

    private fun initView() {
        toolbar.title = args.movie.title

        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        infoRecyclerView.apply {
            addItemDecoration(VerticalDividerItemDecoration(context))
            layoutManager = manager
            isNestedScrollingEnabled = false
            adapter = MovieExternalInfoAdapter { movieExternalInfo -> viewModel.onExternalInfoClick(movieExternalInfo)  }
        }
        infoRecyclerView.layoutManager = manager
        infoRecyclerView.isNestedScrollingEnabled = false
        infoRecyclerView.adapter = MovieExternalInfoAdapter { movieExternalInfo -> viewModel.onExternalInfoClick(movieExternalInfo)  }

        errorMessageView.tryAgainButton.setOnClickListener { viewModel.onTryAgainClick() }

        favImageView.setOnClickListener { viewModel.toggleFavMovie() }
    }
}