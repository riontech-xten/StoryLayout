package com.xtensolutions.storylayout.extensions

import android.content.Context
import android.util.TypedValue


/**
 * Created by Vaghela Mithun R. on 09/02/21.
 * vaghela.mithun@gmail.com
 */

fun Context.getScreenWidth(): Int {
    val metrics = this.resources.displayMetrics
    return metrics.widthPixels
}

fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}

