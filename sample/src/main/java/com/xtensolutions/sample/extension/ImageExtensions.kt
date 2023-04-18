package com.xtensolutions.sample.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.xtensolutions.sample.R

fun AppCompatImageView.loadImageFromUrl(imageUrl: String?) {
    if (imageUrl.isNullOrEmpty()) {
        setDefaultDrawable(this)
    } else {
        Glide.with(this.context)
            .load(imageUrl)
            .placeholder(getProgressDrawable(this.context))
            .transform(RoundedCorners(16))
            .into(this)
    }
}

//code repitation
private fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 2f
        centerRadius = 35f
        setColorSchemeColors(Color.parseColor("#049BD3"))
        start()
    }
}

private fun setDefaultDrawable(imageView: AppCompatImageView) {
    imageView.setImageDrawable(
        ContextCompat.getDrawable(
            imageView.context,
            R.drawable.image_placeholder
        )
    )
}

fun AppCompatImageView.loadBitmap(imageUrl: String?, onLoad: () -> Unit) {
    if (imageUrl.isNullOrEmpty()) {
        setDefaultDrawable(this)
    } else {
        Glide.with(this.context)
            .asBitmap()
            .load(imageUrl)
            .placeholder(getProgressDrawable(this.context))
            .priority(Priority.IMMEDIATE)
            .listener(requestBitmap(onLoad)).submit()
    }
}

private fun AppCompatImageView.requestBitmap(onReady: () -> Unit) =
    object : RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            onReady.invoke()
            setDefaultDrawable(this@requestBitmap)
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onReady.invoke()
            resource?.let {
                this@requestBitmap.setImageBitmap(resource)
            } ?: setDefaultDrawable(this@requestBitmap)
            return true
        }

    }

