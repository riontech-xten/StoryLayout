package com.xtensolutions.storylayout.extensions

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build


/**
 * Created by Vaghela Mithun R. on 10/02/21.
 * vaghela.mithun@gmail.com
 */

@Suppress("DEPRECATION")
fun Drawable.filterColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}