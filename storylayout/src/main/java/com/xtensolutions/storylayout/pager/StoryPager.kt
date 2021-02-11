package com.xtensolutions.storylayout.pager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.xtensolutions.storylayout.listener.StoryActionListener
import com.xtensolutions.storylayout.getScreenWidth


/**
 * Created by Vaghela Mithun R. on 09/02/21.
 * vaghela.mithun@gmail.com
 */
class StoryPager : ViewPager {
    private val TAG: String = StoryPager::class.java.name
    private lateinit var storyActionListener: StoryActionListener
    private var startTime: Long = 0L

    constructor(context: Context?, storyActionListener: StoryActionListener) :
            super(context!!) {
        this.storyActionListener = storyActionListener
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
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "ACTION_DOWN: ")
                startTime = System.currentTimeMillis()
                storyActionListener.onHold()
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "ACTION_UP: ")
                if (System.currentTimeMillis() - startTime > 2000) {
                    storyActionListener.onRelease()
                } else {
                    onSingleTap(event.x)
                }
                startTime = 0
                return true
            }
            MotionEvent.ACTION_BUTTON_RELEASE -> {
                Log.d(TAG, "ACTION_BUTTON_RELEASE: ")
                storyActionListener.onRelease()
                return true
            }
        }

        return false
    }

    private fun onSingleTap(x: Float) {
        Log.d(TAG, "onSingleTapClicked: ")
        if (x < context.getScreenWidth() / 2) {
            storyActionListener.onPrevious()
        } else {
            storyActionListener.onNext()
        }
    }
}