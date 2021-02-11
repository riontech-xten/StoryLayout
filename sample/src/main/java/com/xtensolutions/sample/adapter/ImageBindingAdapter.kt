package com.xtensolutions.sample.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("resId")
    fun setImageResource(view: ImageView, resId: Int) {

        Glide.with(view.context)
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(view)
    }
}