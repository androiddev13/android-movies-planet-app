package com.example.moviesplanet.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.R
import com.example.moviesplanet.presentation.MovieDetailsNavigation
import com.example.moviesplanet.presentation.generic.LiveDataEventObserver
import com.example.moviesplanet.presentation.generic.VerticalDividerItemDecoration
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_my_favorites.*
import javax.inject.Inject

class MyFavoritesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MyFavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel = ViewModelProvider(this, viewModelFactory).get(MyFavoritesViewModel::class.java)

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