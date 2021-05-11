package com.xtensolutions.storylayout.pager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.xtensolutions.storylayout.getScreenWidth
import com.xtensolutions.storylayout.listener.OnStoryChangeListener


/**
 * Created by Vaghela Mithun R. on 09/02/21.
 * vaghela.mithun@gmail.com
 */
class StoryPager : ViewPager {
    //    private val TAG: String = "StoryPager"
    private var storyChangeListener: OnStoryChangeListener? = null
    private var startTime: Long = 0L
    private var isRestarted = false

    constructor(context: Context?, storyChangeListener: OnStoryChangeListener) :
            super(context!!) {
        this.storyChangeListener = storyChangeListener
    }

    constructor(context: Context?, attr: AttributeSet?) : super(context!!, attr)

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Never allow swiping to switch between pages
        return handleTouchEvent(event)
    }

    private fun handleTouchEvent(event: MotionEvent?): Boolean {
        if (storyChangeListener != null) {
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    startTime = System.currentTimeMillis()
                    storyChangeListener!!.onHold(currentItem)
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    if (System.currentTimeMillis() - startTime > 2000) {
                        storyChangeListener!!.onRelease(currentItem)
                    } else {
                        onSingleTap(event.x)
                    }
                    startTime = 0
                    return true
                }
                MotionEvent.ACTION_BUTTON_RELEASE -> {
                    storyChangeListener!!.onRelease(currentItem)
                    return true
                }
            }
        }

        return false
    }

    private fun onSingleTap(x: Float) {
        if (storyChangeListener != null) {
            if (x < context.getScreenWidth() / 2) {
                currentItem = if (currentItem > 0) currentItem - 1 else 0
                storyChangeListener!!.onPrevious(currentItem)
            } else {
                if (adapter != null) {
                    if (currentItem < adapter!!.count - 1) {
                        currentItem += 1
                        isRestarted = false
                        storyChangeListener!!.onNext(currentItem)
                    } else if (!isRestarted) {
                        isRestarted = true
                        storyChangeListener!!.onRestartStory(0)
                    }
                }
            }
        }
    }
}