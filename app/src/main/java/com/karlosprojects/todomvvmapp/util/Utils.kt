package com.karlosprojects.todomvvmapp.util

/**
 * This is an extension property, that just return the same object. Basically doesn't do anything,
 * but it can turn a statement into an expression
 */
val <T> T.exhaustive: T
    get() = this