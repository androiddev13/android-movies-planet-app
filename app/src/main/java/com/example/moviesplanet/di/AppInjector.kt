package com.example.moviesplanet.di

import com.example.moviesplanet.AndroidApplication
import com.example.moviesplanet.di.component.DaggerAppComponent

object AppInjector {

    fun init(application: AndroidApplication) {
        DaggerAppComponent.builder()
            .application(application)
            .build()
            .inject(application)
    }

}