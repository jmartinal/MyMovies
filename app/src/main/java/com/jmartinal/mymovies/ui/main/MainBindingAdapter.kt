package com.jmartinal.mymovies.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jmartinal.domain.Movie

@BindingAdapter("items")
fun RecyclerView.setItems(items: List<Movie>?) {
    (adapter as? MoviesAdapter)?.let {
        it.movies = items ?: emptyList()
    }
}