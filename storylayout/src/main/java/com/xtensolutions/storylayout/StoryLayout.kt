package com.xtensolutions.storylayout

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.xtensolutions.storylayout.extensions.convertDpToPx
import com.xtensolutions.storylayout.extensions.filterColor
import com.xtensolutions.storylayout.extensions.getScreenWidth
import com.xtensolutions.storylayout.extensions.match
import com.xtensolutions.storylayout.listener.OnStoryChangeListener
import com.xtensolutions.storylayout.pager.StoryPager
import com.xtensolutions.storylayout.timer.StoryTimer
import kotlin.math.roundToInt


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

    companion object {
        private const val DEFAULT_DELAY_TIME = 60000L
    }

    // default attrs
    var maxDuration = DEFAULT_DELAY_TIME
        set(value) {
            field = value
            storyTimer.delayTime = value
        }

    var prgIndColor = Color.WHITE
    var prgIndSpace = 4f
    var prgIndHorizontalPadding = 8f
    var prgIndVerticalPadding = 4f
    var prgAllowCircular = false
    var prgAlignment = 1

    // A flag for start each story after content loaded or start immediately
    // If set true then method contentReady should be invoke manually
    // If set false story will start automatically
//    var waitWhileLoading: Boolean = false
//        set(value) {
//            field = value
//            storyTimer.waitWhileLoading = value
//        }

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

    var durations: List<Long>? = null

    var adapter: RecyclerView.Adapter<*>? = null
        set(value) {
            field = value
            buildStoryView()
            storyPager.adapter = value
        }

    init {
        set.clone(this)
        storyTimer = StoryTimer()
    }

    override fun isInEditMode(): Boolean = true

    private fun initAttributes(attrs: AttributeSet) {
        val attr = context.theme.obtainStyledAttributes(attrs, R.styleable.StoryLayout, 0, 0)

        maxDuration = attr.getInt(
            R.styleable.StoryLayout_delayTime, maxDuration.toInt()
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

        prgAlignment = attr.getInt(
            R.styleable.StoryLayout_prgAlignment,
            prgAlignment
        )

//        waitWhileLoading = attr.getBoolean(R.styleable.StoryLayout_prgAutoStart, waitWhileLoading)

        attr.recycle()
        setupStoryPager()
        setupProgressContainer()
    }

    /**
     * setup auto scrolling story slider
     */
    private fun setupStoryPager() {
        storyPager = StoryPager(context, this)
        storyPager.pager.layoutParams = getDefaultParams(matchParent, matchParent).apply {
            bottomToBottom = ConstraintSet.PARENT_ID
        }

        addView(storyPager.pager)
        set.match(storyPager.pager, this)
    }

    /**
     * setup story progress indicator container
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun setupProgressContainer() {
        val params = if (prgAlignment == 1) getDefaultParams(matchParent, wrapContent)
        else getDefaultParams(matchParent, wrapContent).apply {
            bottomToBottom = ConstraintSet.PARENT_ID
            topToTop = ConstraintSet.UNSET
        }

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
        if (durations == null) {
            progressContainer.weightSum = adapter!!.itemCount.toFloat()
        }
        progressContainer.removeAllViews()
        progressIndicators.clear()

        generateProgressBarWidth {
            it.forEachIndexed { index, width ->
                generateProgressIndicator(index, width)
            }
        }

        storyTimer.delayTime = maxDuration
        storyTimer.timerUpdateListener = this
        storyTimer.start()
    }

    private fun getSpace(index: Int): Int {
        return if (index == adapter!!.itemCount - 1) 0 else prgIndSpace.toInt()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun generateProgressIndicator(index: Int, prgWidth: Int) {
        println("weight => $prgWidth")
        ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
            progress = 0
            max = 100
            layoutParams = LinearLayout.LayoutParams(
                if (durations == null) 0 else prgWidth,
                context.convertDpToPx(2f).toInt(),
                if (durations == null) 1.0f else 0f
            ).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    marginEnd = getSpace(index)
                } else {
                    setMargins(0, 0, getSpace(index), 0)
                }
            }

            val withAlpha = ColorUtils.setAlphaComponent(prgIndColor, 180)
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
     * Generating the [ProgressBar] width as per it's duration size
     */
    private fun generateProgressBarWidth(block: (List<Int>) -> Unit) {
        var progressWidths = arrayListOf<Int>()
        adapter?.let {
            val containerSize = getProgressContainerSize().roundToInt()
            var assignedSize = 0
            for (i in 0 until it.itemCount) {
                val width = getProgressWidth(i, it.itemCount, containerSize)
                assignedSize += width
                progressWidths.add(width)
            }

            progressWidths = distributesSizeDifferenceBetweenAllProgress(
                progressWidths,
                containerSize,
                assignedSize
            ) as ArrayList<Int>
        }
        block(progressWidths)
    }

    /**
     * Find the difference between total assigned and container size,
     * distributes difference equally between all progress child
     * @param sizes calculated progress size array
     * @param containerSize progress container total size
     * @param totalAssignedSize sum of assigned progress size
     */
    private fun distributesSizeDifferenceBetweenAllProgress(
        sizes: List<Int>,
        containerSize: Int,
        totalAssignedSize: Int
    ): List<Int> {
        if (totalAssignedSize < containerSize && sizes.isNotEmpty()) {
            val difference = containerSize - totalAssignedSize
            val equalSize = difference / sizes.size
            var equalTotal = 0
            val finalList = sizes.map {
                equalTotal += equalSize
                it + equalSize
            }
            // assign final difference to last child if any reminder
            val finalDiff = difference - equalTotal
            finalList.last().plus(finalDiff)
            return finalList
        }

        return sizes
    }

    /**
     * To get or generate progress bar item size or weight as per its duration
     * @param index current progressbar index
     * @param count total number of [ProgressBar]
     * @param containerSize main [ProgressBar] container [LinearLayout] size
     * @return weight Float
     */
    private fun getProgressWidth(index: Int, count: Int, containerSize: Int): Int {
        val maxSize = (containerSize / count)
        return durations?.let { duration ->
            val totalSize = maxDuration * count
            if (index < duration.size && duration[index] <= maxDuration) {
                val mediaSize = duration[index]
                ((mediaSize * containerSize) / totalSize).toFloat().roundToInt()
            } else maxSize
        } ?: return maxSize
    }

    /**
     * Calculating the exact size of [progressContainer] after removed padding and between child space
     * @return actual with in Float
     */
    private fun getProgressContainerSize(): Float {
        return context.getScreenWidth() - ((prgIndSpace * adapter!!.itemCount - 2) + (prgIndHorizontalPadding * 2))
    }

    /**
     * call when user tap for next story
     */
    override fun onNext(position: Int) {
        if (progressIndicators.size > 0 && position < progressIndicators.size) {
            progressIndicators[position - 1].progress = 100
//            storyTimer.waitWhileLoading = waitWhileLoading
            if (::storyChangeListener.isInitialized)
                storyChangeListener.onNext(position)
            storyTimer.restart()
        }

//        Log.d(TAG, "onNext($position) ")
    }

    /**
     * call when user tap for previous story
     */
    override fun onPrevious(position: Int) {
        if (progressIndicators.size > 0) {
            progressIndicators[position].progress = 0
            progressIndicators[position + 1].progress = 0
//            storyTimer.waitWhileLoading = waitWhileLoading
            if (::storyChangeListener.isInitialized)
                storyChangeListener.onPrevious(position)
            storyTimer.restart()
        }

//        Log.d(TAG, "onPrevious($position) ")
    }

    /**
     * call when user hold the story view
     */
    override fun onHold(position: Int) {
        if (::storyChangeListener.isInitialized)
            storyChangeListener.onHold(position)
        storyTimer.pause()
//        Log.d(TAG, "onHold($position) ")
    }

    /**
     * call to release view after view hold by user tap
     */
    override fun onRelease(position: Int) {
        if (::storyChangeListener.isInitialized)
            storyChangeListener.onRelease(position)
        storyTimer.resume()
//        Log.d(TAG, "onRelease($position): ")
    }

    override fun onRestartStory(position: Int) {
//        Log.d("StoryLayout", "onRestartStory")
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

//        Log.d(TAG, "onRestartStory($position): ")
    }

    /**
     * call when each story completed
     * and target for next story or finish the story
     */
    override fun onCompleted() {
//        Log.d(TAG, "onCompleted")
        progressIndicators[storyPager.currentItem].progress = 100
        if (storyPager.currentItem < adapter!!.itemCount - 1) {
            storyPager.currentItem += 1
            onNext(storyPager.currentItem)
        } else {
            onRestartStory(0)
        }
    }

    /**
     * call to update the progress indicator
     */
    override fun onUpdate(interval: Long) {
        val progress = 100 - ((interval * 100) / maxDuration)
        progressIndicators[storyPager.currentItem].progress = progress.toInt()
    }

    override fun onSwipeLeftToRight(position: Int) {
        super.onSwipeLeftToRight(position)
        storyChangeListener.onSwipeLeftToRight(position)
    }

    override fun onSwipeRightToLeft(position: Int) {
        super.onSwipeRightToLeft(position)
        storyChangeListener.onSwipeRightToLeft(position)
    }

    override fun onSwipeDown(position: Int) {
        super.onSwipeDown(position)
        storyChangeListener.onSwipeDown(position)
    }

    override fun onSwipeUp(position: Int) {
        super.onSwipeUp(position)
        storyChangeListener.onSwipeUp(position)
    }

    fun hold() {
        if (adapter != null && adapter!!.itemCount > 0)
            onHold(storyPager.currentItem)
    }

    fun release() {
        if (adapter != null && adapter!!.itemCount > 0)
            onRelease(storyPager.currentItem)
    }

    fun stop() = storyTimer.stop()

    fun isStopped() = storyTimer.isStopped()

    fun isPause() = storyTimer.isPause()

//    @SuppressLint("VisibleForTests")
//    fun disableTimerWhileTesting(enable: Boolean) = storyTimer.disableTimerWhileTesting(enable)

    fun start() = storyTimer.start()

    fun reset() = storyTimer.restart()

    /**
     * To start story when content ready to play after wait for load
     */
//    fun contentReady() {
//        waitWhileLoading = false
//        storyTimer.waitWhileLoading = waitWhileLoading
//        start()
//    }
}
