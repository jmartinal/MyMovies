package com.jmartinal.mymovies.ui.detail

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jmartinal.mymovies.R

@BindingAdapter("favorite")
fun FloatingActionButton.setFavorite(favorite: Boolean?) {
    val icon = if (favorite == true) {
        R.drawable.ic_favorite_on
    } else {
        R.drawable.ic_favorite_off
    }
    setImageDrawable(context.getDrawable(icon))
}