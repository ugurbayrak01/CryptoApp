package com.ugurbayrak.cryptoapp.util

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ugurbayrak.cryptoapp.R

fun ImageView.downloadFromUrl(url: String?, progressDrawable: CircularProgressDrawable?) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_launcher_foreground)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

@BindingAdapter("android:download_detail_logo")
fun downloadDetailLogo(view: ImageView, url: String?) {
    view.downloadFromUrl(url, placeholderProgressBar(view.context))
}

@BindingAdapter("android:download_logo")
fun downloadLogo(view: ImageView, url: String?) {
    view.downloadFromUrl(url, null)
}

fun placeholderProgressBar(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 32f
        setColorSchemeColors(ContextCompat.getColor(context, R.color.white))
        start()
    }
}