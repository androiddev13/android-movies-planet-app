package com.example.moviesplanet.presentation.generic

import androidx.lifecycle.Observer

/**
 * An [Observer] for [LiveDataEvent]s, simplifying the pattern of checking if the [LiveDataEvent]'s content has
 * already been handled.
 */
class LiveDataEventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<LiveDataEvent<T>> {
    override fun onChanged(event: LiveDataEvent<T>?) {
        event?.getContentIfNotHandled()?.let(onEventUnhandledContent)
    }
}