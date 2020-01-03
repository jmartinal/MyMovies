package com.jmartinal.mymovies.ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.jmartinal.mymovies.ui.common.loadUrl

@BindingAdapter("url")
fun ImageView.bindUrl(url: String?) {
    if (url != null) {
        loadUrl(url)
    }
}

@BindingAdapter("visible")
fun View.setVisible(isVisible: Boolean?) {
    visibility = if (isVisible == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}