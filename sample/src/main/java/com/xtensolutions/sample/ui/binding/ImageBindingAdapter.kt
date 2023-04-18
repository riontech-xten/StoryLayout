package com.xtensolutions.sample.ui.binding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.xtensolutions.sample.extension.loadImageFromUrl


/**
 * Created by Vaghela Mithun R. on 14-02-2023 - 00:21.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@BindingAdapter("android:loadImageFromUrl")
fun bindImageFromUrl(view: AppCompatImageView, imageUrl: String?) {
    view.loadImageFromUrl(imageUrl)
}