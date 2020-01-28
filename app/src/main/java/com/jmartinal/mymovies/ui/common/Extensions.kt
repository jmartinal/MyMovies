package com.jmartinal.mymovies.ui.common

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T: ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {
    val vmFactory = object: ViewModelProvider.Factory {
        override fun <U : ViewModel?> create(modelClass: Class<U>): U = factory() as U
    }
    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}