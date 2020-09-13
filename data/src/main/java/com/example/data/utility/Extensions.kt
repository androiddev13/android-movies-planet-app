package com.example.data.utility

/**
 * By default, Kotlin doesn't care if all branches are handled in a when statement. However, if you
 * use the when statement as an expression (with a value) it will force all cases to be handled.
 * This helper is to make a lightweight way to say you meant to match all of them.
 */
val <T> T.exhaustive: T
    get() = this