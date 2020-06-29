package com.example.moviesplanet

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnitRunner
import com.example.moviesplanet.presentation.movies.MoviesActivity
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesActivityTest : AndroidJUnitRunner() {

    @get:Rule
    val activityTestRule = ActivityTestRule<MoviesActivity>(MoviesActivity::class.java)

    override fun onStart() {
        RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("RxJava 2.x IO Scheduler"));
        super.onStart()
    }

    @Test
    fun movieItemClick_MovieDetailsIsDisplayed() {
        onView(withId(R.id.mainRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.movieDetailsContainer)).check(matches(isDisplayed()))
    }

    @Test
    fun settingsMenuItemClick_SettingsIsDisplayed() {
        onView(withId(R.id.drawerLayout)).check(matches(isClosed())).perform(DrawerActions.open())
        onView(withId(R.id.navigationView)).perform(NavigationViewActions.navigateTo(R.id.menuSetting))
        onView(withId(R.id.settingsContainer)).check(matches(isDisplayed()))
    }

    @Test
    fun myFavoritesMenuItemClick_MyFavoritesIsDisplayed() {
        onView(withId(R.id.drawerLayout)).check(matches(isClosed())).perform(DrawerActions.open())
        onView(withId(R.id.navigationView)).perform(NavigationViewActions.navigateTo(R.id.menuFavorites))
        onView(withId(R.id.myFavoritesContainer)).check(matches(isDisplayed()))
    }
}