package com.xtensolutions.storylayout.listener


/**
 * Created by Vaghela Mithun R. on 09/02/21.
 * vaghela.mithun@gmail.com
 */
interface OnStoryChangeListener {
    fun onNext(position: Int)
    fun onPrevious(position: Int)
    fun onHold(position: Int)
    fun onRelease(position: Int)
    fun onRestartStory(position: Int)
    fun onSwipeRightToLeft(position: Int) {}
    fun onSwipeLeftToRight(position: Int) {}
    fun onSwipeDown(position: Int) {}
    fun onSwipeUp(position: Int) {}
}