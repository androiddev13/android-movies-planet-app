package com.example.moviesplanet.presentation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.R
import com.example.moviesplanet.presentation.generic.VerticalDividerItemDecoration
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initView()

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.title_settings)

        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        settingsRecyclerView.apply {
            layoutManager = manager
            addItemDecoration(VerticalDividerItemDecoration(context))
            //adapter = SettingsAdapter {  } // TODO
        }
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

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}