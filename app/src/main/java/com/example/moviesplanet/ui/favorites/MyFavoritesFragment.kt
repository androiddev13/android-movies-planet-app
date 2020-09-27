package com.example.moviesplanet.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.R
import com.example.moviesplanet.ui.MovieDetailsNavigation
import com.example.moviesplanet.ui.generic.LiveDataEventObserver
import com.example.moviesplanet.ui.generic.VerticalDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_favorites.*

@AndroidEntryPoint
class MyFavoritesFragment : Fragment() {

    private val viewModel: MyFavoritesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.favoriteMoviesLiveData.observe(viewLifecycleOwner, Observer {
            (moviesRecyclerView.adapter as MyFavoritesAdapter).setData(it)
            val noFavoritesViewVisibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            noFavoritesTextView.visibility = noFavoritesViewVisibility
        })

        viewModel.favoritesNavigationLiveData.observe(viewLifecycleOwner, LiveDataEventObserver {
            when (it) {
                is MovieDetailsNavigation -> {
                    val action = MyFavoritesFragmentDirections.actionMyFavoritesFragmentToMovieDetailsFragment(it.movie)
                    findNavController().navigate(action)
                }
            }
        })
    }

    private fun initView() {
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        moviesRecyclerView.apply {
            layoutManager = manager
            addItemDecoration(VerticalDividerItemDecoration(context))
            adapter = MyFavoritesAdapter {
                viewModel.onMovieClick(it)
            }
        }
    }
}