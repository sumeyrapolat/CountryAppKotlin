package com.sumeyra.kotlincountry.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sumeyra.kotlincountry.R

//imageView diğer datalardan daha geç geleceği için içerisine bir placceHolder konulması istenir


fun ImageView.downloadFromUrl(url: String?, circularProgressDrawable: CircularProgressDrawable){

    val options = RequestOptions()
        .placeholder(circularProgressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun placeHolderProgressBar(context: Context) : CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth =8f
        centerRadius = 40f
        start()
    }
}
