package com.xtensolutions.storylayout

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.ColorUtils
import androidx.viewpager.widget.PagerAdapter
import com.xtensolutions.storylayout.extensions.filterColor
import com.xtensolutions.storylayout.extensions.match
import com.xtensolutions.storylayout.listener.OnStoryChangeListener
import com.xtensolutions.storylayout.pager.StoryPager
import com.xtensolutions.storylayout.timer.StoryTimer


/**
 * Created by Vaghela Mithun R. on 09/02/21.
 * vaghela.mithun@gmail.com
 *
 * StoryLayout is a child class of ConstraintLayout,
 * created to achieve the concept like WhatsApp status or
 * Instagram story. Designed with the help of ViewPager,
 * LinearLayout and ProgressBar components which are child
 * view of StoryLayout class.
 */

class StoryLayout : ConstraintLayout, OnStoryChangeListener, StoryTimer.OnTimerUpdateListener {

    private var matchParent = LayoutParams.MATCH_PARENT
    private var wrapContent = LayoutParams.WRAP_CONTENT

    private lateinit var progressContainer: LinearLayout
    private lateinit var storyPager: StoryPager
    private var storyTimer: StoryTimer
    private var progressIndicators: MutableList<ProgressBar> = ArrayList()
    private val set = ConstraintSet()

    // default attrs
    var delayTime = 3000L
    var prgIndColor = Color.WHITE
    var prgIndSpace = 4f
    var prgIndHorizontalPadding = 8f
    var prgIndVerticalPadding = 4f
    var prgAllowCircular = false

    lateinit var storyChangeListener: OnStoryChangeListener

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attr: AttributeSet?) : super(context!!, attr) {
        initAttributes(attr!!)
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttributes(attrs!!)
    }

    var adapter: PagerAdapter? = null
        get() = field
        set(value) {
            field = value
            buildStoryView()
            storyPager.adapter = value
        }

    init {
        set.clone(this)
        storyTimer = StoryTimer(delayTime)
        storyTimer.timerUpdateListener = this
    }

    private fun initAttributes(attrs: AttributeSet) {
        val attr = context.theme.obtainStyledAttributes(attrs, R.styleable.StoryLayout, 0, 0)

        delayTime = attr.getInt(
            R.styleable.StoryLayout_delayTime, delayTime.toInt()
        ).toLong()

        prgIndColor = attr.getColor(
            R.styleable.StoryLayout_prgIndColor,
            prgIndColor
        )

        prgIndSpace = attr.getDimension(
            R.styleable.StoryLayout_prgIndSpace,
            prgIndSpace
        )

        prgIndHorizontalPadding = attr.getDimension(
            R.styleable.StoryLayout_indHorizontalPadding,
            prgIndHorizontalPadding
        )

        prgIndVerticalPadding = attr.getDimension(
            R.styleable.StoryLayout_indVerticalPadding,
            prgIndVerticalPadding
        )

        prgAllowCircular = attr.getBoolean(
            R.styleable.StoryLayout_prgAllowCircular,
            prgAllowCircular
        )

        attr.recycle()
        setupStoryPager()
        setupProgressContainer()
    }

    /**
     * setup auto scrolling story slider
     */
    private fun setupStoryPager() {
        storyPager = StoryPager(context, this)
        storyPager.layoutParams = getDefaultParams(matchParent, matchParent).apply {
            bottomToBottom = ConstraintSet.PARENT_ID
        }

        addView(storyPager)
        set.match(storyPager, this)
    }

    /**
     * setup story progress indicator container
     */
    private fun setupProgressContainer() {
        val params = getDefaultParams(matchParent, wrapContent)
        LinearLayout(context).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = 8f
            }
            orientation = LinearLayout.HORIZONTAL
            params.setMargins(0, prgIndVerticalPadding.toInt(), 0, 0)
            layoutParams = params
        }.let {
            progressContainer = it

            progressContainer.setPadding(
                prgIndHorizontalPadding.toInt(),
                prgIndVerticalPadding.toInt(),
                prgIndHorizontalPadding.toInt(),
                prgIndVerticalPadding.toInt()
            )
            addView(progressContainer)
            set.match(progressContainer, this@StoryLayout)

        }
        invalidate()
    }

    /**
     * default layout params for child view
     */
    private fun getDefaultParams(width: Int, height: Int): LayoutParams {
        return LayoutParams(width, height).apply {
            topToTop = ConstraintSet.PARENT_ID
            startToStart = ConstraintSet.PARENT_ID
            endToEnd = ConstraintSet.PARENT_ID
        }
    }

    /**
     * building story view with use of pager adapter
     */
    private fun buildStoryView() {
        progressContainer.weightSum = adapter!!.count.toFloat()
        progressContainer.removeAllViews()
        for (i in 0..adapter!!.count) {
            generateProgressIndicator(i)
        }

        storyTimer.start()
    }

    private fun getSpace(index: Int): Int {
        return if (index == adapter!!.count - 1) 0 else prgIndSpace.toInt()
    }

    private fun generateProgressIndicator(index: Int) {
        ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
            progress = 0
            max = 100
            layoutParams = LinearLayout.LayoutParams(
                0, context.convertDpToPx(2f).toInt(),
                1.0f
            ).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    marginEnd = getSpace(index)
                } else {
                    setMargins(0, 0, getSpace(index), 0)
                }
            }

            val withAlpha = ColorUtils.setAlphaComponent(prgIndColor, 120)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressTintList = ColorStateList.valueOf(prgIndColor)
                indeterminateTintList = ColorStateList.valueOf(withAlpha)
            } else {
                progressDrawable.filterColor(prgIndColor)
                indeterminateDrawable.filterColor(withAlpha)
            }
        }.let {
            progressContainer.addView(it)
            progressIndicators.add(it)
        }
    }

    /**
     * call when user tap for next story
     */
    override fun onNext(position: Int) {
        if (progressIndicators.size > 0 && position < progressIndicators.size) {
            progressIndicators[position - 1].progress = 100
            storyTimer.restart()
            if (::storyChangeListener.isInitialized)
                storyChangeListener.onNext(position)
        }
    }

    /**
     * call when user tap for previous story
     */
    override fun onPrevious(position: Int) {
        if (progressIndicators.size > 0) {
            progressIndicators[position].progress = 0
            progressIndicators[position + 1].progress = 0
            storyTimer.restart()
            if (::storyChangeListener.isInitialized)
                storyChangeListener.onPrevious(position)
        }
    }

    /**
     * call when user hold the story view
     */
    override fun onHold(position: Int) {
        storyTimer.pause()
        if (::storyChangeListener.isInitialized)
            storyChangeListener.onHold(position)
    }

    /**
     * call to release view after view hold by user tap
     */
    override fun onRelease(position: Int) {
        storyTimer.resume()
        if (::storyChangeListener.isInitialized)
            storyChangeListener.onRelease(position)
    }

    override fun onRestartStory(position: Int) {
        Log.d("StoryLayout", "onRestartStory")
        storyTimer.stop()
        if (prgAllowCircular) {
            progressIndicators.forEach {
                it.progress = 0
            }

            storyPager.currentItem = position
            storyTimer.restart()
        }

        if (::storyChangeListener.isInitialized)
            storyChangeListener.onRestartStory(position)
    }

    /**
     * call when each story completed
     * and target for next story or finish the story
     */
    override fun onCompleted() {
        Log.d("StoryLayout", "onCompleted")
        progressIndicators[storyPager.currentItem].progress = 100
        if (storyPager.currentItem < adapter!!.count - 1) {
            storyPager.currentItem += 1
            onNext(storyPager.currentItem)
        } else {
            storyTimer.stop()
            onRestartStory(0)
        }
    }

    /**
     * call to update the progress indicator
     */
    override fun onUpdate(interval: Long) {
        val progress = 100 - ((interval * 100) / delayTime)
        progressIndicators[storyPager.currentItem].progress = progress.toInt()
    }

    fun hold() {
        if (adapter != null && adapter!!.count > 0)
            onHold(storyPager.currentItem)
    }

    fun release() {
        if (adapter != null && adapter!!.count > 0)
            onRelease(storyPager.currentItem)
    }

    fun stop() = storyTimer.stop()
}
