package com.example.moviesplanet.di.component

import android.app.Application
import com.example.moviesplanet.AndroidApplication
import com.example.moviesplanet.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(modules = [AndroidInjectionModule::class, BuildersModule::class, NetworkModule::class,
    DataModule::class, ViewModelModule::class, ViewModelFactoryModule::class, PreferencesModule::class,
    DatabaseModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(application: AndroidApplication)

}