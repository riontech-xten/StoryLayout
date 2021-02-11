package com.xtensolutions.storylayout.listener


/**
 * Created by Vaghela Mithun R. on 09/02/21.
 * vaghela.mithun@gmail.com
 */
interface StoryActionListener {
    fun onNext()
    fun onPrevious()
    fun onHold()
    fun onRelease()
}