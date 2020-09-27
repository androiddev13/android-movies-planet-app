package com.example.moviesplanet.presentation.movies

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.data.model.Status
import com.example.moviesplanet.R
import com.example.moviesplanet.presentation.MovieDetailsNavigation
import com.example.moviesplanet.presentation.MyFavoritesNavigation
import com.example.moviesplanet.presentation.SettingsNavigation
import com.example.moviesplanet.presentation.generic.LiveDataEventObserver
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_movies_content.*
import kotlinx.android.synthetic.main.view_error_message.view.*
import javax.inject.Inject

class MoviesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MoviesViewModel

    private lateinit var actionBarToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel = ViewModelProvider(this, viewModelFactory).get(MoviesViewModel::class.java)

        viewModel.moviesLiveData.observe(viewLifecycleOwner, Observer {
            (mainRecyclerView.adapter as MoviesAdapter).setData(it)
        })

        viewModel.moviesLoadingStatusLiveData.observe(viewLifecycleOwner, Observer {
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

        viewModel.moviesNavigationLiveData.observe(viewLifecycleOwner, LiveDataEventObserver {
            when (it) {
                is MovieDetailsNavigation -> {
                    val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(it.movie)
                    findNavController().navigate(action)
                }
                is MyFavoritesNavigation -> {
                    val action = MoviesFragmentDirections.actionMoviesFragmentToMyFavoritesFragment()
                    findNavController().navigate(action)
                }
                is SettingsNavigation -> {  } // TODO
            }
        })
    }

    private fun initView() {
        toolbar.apply {
            inflateMenu(R.menu.menu_movies)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.optionPopular -> {
                        viewModel.sortByPopularClick()
                        true
                    }
                    R.id.optionRate -> {
                        viewModel.sortByTopRatedClick()
                        true
                    }
                    R.id.optionUpcoming -> {
                        viewModel.sortByUpcomingClick()
                        true
                    }
                    else -> false
                }
            }
        }

        actionBarToggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, toolbar,0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mainRecyclerView.layoutManager = manager
        mainRecyclerView.adapter = MoviesAdapter({ movie -> viewModel.onMovieClick(movie) }, { viewModel.tryAgainClick() })

        errorMessageView.tryAgainButton.setOnClickListener { viewModel.tryAgainClick() }

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuFavorites -> viewModel.onMyFavoritesClick()
                R.id.menuSetting -> viewModel.onSettingsClick()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}