package com.example.moviesplanet

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnitRunner
import com.example.moviesplanet.presentation.moviedetails.MovieDetailsActivity
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Rule
import org.junit.Test

class MovieDetailsActivityTest : AndroidJUnitRunner() {

    @get:Rule
    val activityTestRule = ActivityTestRule<MovieDetailsActivity>(MovieDetailsActivity::class.java)

    override fun onStart() {
        RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("RxJava 2.x IO Scheduler"));
        super.onStart()
    }

    @Test
    fun movieItemClick_MovieDetailsIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.mainRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
        Espresso.onView(ViewMatchers.withId(R.id.movieDetailsContainer))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}