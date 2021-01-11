package com.karlosprojects.todomvvmapp.util

import androidx.appcompat.widget.SearchView

/**
 * This is an extension function on SearchView, so later in our fragment we can use it to search, but only use
 * one function.
 * We use inline modifier just for efficiency (it's not important for the functionality but for efficiency), because
 * when we pass such a function parameter, the kotlin code actually generates separate object for 'it' and this create
 * runtine overhead
 * With crossinline we tell the compiler that later we will not allow to code return
 * Inline is efficiency, crossinline is necessary
 */
inline fun SearchView.onQueryTextChange(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}