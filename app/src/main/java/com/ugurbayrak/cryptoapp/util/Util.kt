package com.ugurbayrak.cryptoapp.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ugurbayrak.cryptoapp.R

fun ImageView.downloadFromUrl(url: String?) {
    val options = RequestOptions()
        .placeholder(R.drawable.ic_launcher_foreground)
        .error(R.drawable.ic_launcher_foreground)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

@BindingAdapter("android:download_url")
fun downloadImage(view: ImageView, url: String?) {
    view.downloadFromUrl(url)
}