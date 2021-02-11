package com.xtensolutions.storylayout.extensions

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet


/**
 * Created by Vaghela Mithun R. on 10/02/21.
 * vaghela.mithun@gmail.com
 */

fun ConstraintSet.match(view: View, parentView: View) {
    this.connect(view.id, ConstraintSet.TOP, parentView.id, ConstraintSet.TOP)
    this.connect(view.id, ConstraintSet.START, parentView.id, ConstraintSet.START)
    this.connect(view.id, ConstraintSet.END, parentView.id, ConstraintSet.END)
    this.connect(view.id, ConstraintSet.BOTTOM, parentView.id, ConstraintSet.BOTTOM)
}