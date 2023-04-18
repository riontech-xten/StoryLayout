package com.xtensolutions.storylayout.pager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.xtensolutions.storylayout.extensions.getScreenWidth
import com.xtensolutions.storylayout.listener.OnStoryChangeListener


/**
 * Created by Vaghela Mithun R. on 09/02/21.
 * vaghela.mithun@gmail.com
 */
@SuppressLint("ClickableViewAccessibility")
class StoryPager(val context: Context, val storyChangeListener: OnStoryChangeListener) :
    View.OnTouchListener {

    val pager: ViewPager2 = ViewPager2(context)
    private var isRestarted = false
    var currentItem: Int
        get() = pager.currentItem
        set(value) {
            pager.currentItem = value
        }

    var adapter: RecyclerView.Adapter<*>?
        get() = pager.adapter
        set(value) {
            pager.adapter = value
        }

    init {
        pager.setOnTouchListener(this)
        pager.isUserInputEnabled = false
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun onSingleTap(x: Float) {
        if (x < context.getScreenWidth() / 2) {
            pager.currentItem = if (pager.currentItem > 0) pager.currentItem - 1 else 0
            storyChangeListener.onPrevious(pager.currentItem)
        } else {
            if (adapter != null) {
                if (pager.currentItem < adapter!!.itemCount - 1) {
                    pager.currentItem += 1
                    isRestarted = false
                    storyChangeListener.onNext(pager.currentItem)
                } else if (!isRestarted) {
                    isRestarted = true
                    storyChangeListener.onRestartStory(0)
                }
            }
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_UP) {
            storyChangeListener.onRelease(pager.currentItem)
        }

        return gestureDetector.onTouchEvent(event)
    }

    private val gestureDetector = GestureDetector(context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                onSingleTap(e.x)
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                storyChangeListener.onHold(pager.currentItem)
                return true
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                Log.d("StoryPager", "onFling")
                // Get swipe delta value in x axis.
                val deltaX = e1.x - e2.x
                // Get swipe delta value in y axis.
                val deltaY = e1.y - e2.y
                // Get absolute value.
                val deltaXAbs = Math.abs(deltaX)
                val deltaYAbs = Math.abs(deltaY)
                // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                if (deltaXAbs in 100.0..1000.0) {
                    if (deltaX > 0) storyChangeListener.onSwipeRightToLeft(pager.currentItem)
                    else storyChangeListener.onSwipeLeftToRight(pager.currentItem)
                    if (deltaX > 0) {
                        Log.d("StoryPager", "Swipe to left")
                    } else {
                        Log.d("StoryPager", "Swipe to right")
                    }
                } else if (deltaYAbs in 100.0..1000.0) {
                    if (deltaY > 0) storyChangeListener.onSwipeDown(pager.currentItem)
                    else storyChangeListener.onSwipeUp(pager.currentItem)
                    if (deltaY > 0) {
                        Log.d("StoryPager", "Swipe to up")
                    } else {
                        Log.d("StoryPager", "Swipe to down")
                    }
                }
                return true
            }
        }
    )

//    companion object {
//        private const val SWIPE_THRESHOLD = 100
//        private const val SWIPE_VELOCITY_THRESHOLD = 1000
//    }
}