package com.xtensolutions.sample.extension

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


/**
 * Created by Vaghela Mithun R. on 03/02/21.
 * vaghela.mithun@gmail.com
 */

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.resourceByName(name: String, type: String): Int {
    return resources.getIdentifier(name, type, packageName)
}

fun Context.drawableFromName(name: String): Int {
    return resourceByName(name, "drawable")
}

fun Context.drawable(drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}

/**
 * Return true if this [Context] is available.
 * Availability is defined as the following:
 * + [Context] is not null
 * + [Context] is not destroyed (tested with [FragmentActivity.isDestroyed] or [Activity.isDestroyed])
 */
fun Context?.isAvailable(): Boolean {
    if (this == null) {
        return false
    } else if (this !is Application) {
        if (this is FragmentActivity) {
            return !this.isDestroyed
        } else if (this is Activity) {
            return !this.isDestroyed
        }
    }
    return true
}

fun Context.fitToScreenWidth(item: Int, @DimenRes dimenPadding: Int): Int {
    val padding = this.resources.getDimensionPixelOffset(dimenPadding)
    return (this.screenWidth() - padding * 2) / item
}

fun Context.screenWidth(): Int {
    val displayMetrics = this.resources.displayMetrics
    return displayMetrics.widthPixels
}

fun Context.screenHeight(): Int {
    val displayMetrics = this.resources.displayMetrics
    return displayMetrics.heightPixels
}

fun Context.screenRatio(): Float {
    return (screenWidth() / screenHeight()).toFloat()
}
