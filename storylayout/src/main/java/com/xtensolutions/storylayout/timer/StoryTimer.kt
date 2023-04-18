package com.xtensolutions.storylayout.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.VisibleForTesting


/**
 * Created by Vaghela Mithun R. on 10/02/21.
 * vaghela.mithun@gmail.com
 */
class StoryTimer {
    companion object {
        const val COUNT_DOWN_DEFAULT_INTERVAL = 5L
    }

    private var isStopped: Boolean = false
    private var isPause: Boolean = false
    var delayTime: Long = 3000L
        set(value) {
            field = value
            timeLeftInMiliseconds = value
        }

    var timeLeftInMiliseconds = delayTime
    lateinit var timerUpdateListener: OnTimerUpdateListener
    private lateinit var timer: CountDownTimer

    @VisibleForTesting
    private var testEnabled = false

//    var waitWhileLoading = false

    interface OnTimerUpdateListener {
        fun onCompleted()
        fun onUpdate(interval: Long)
    }

    fun start() {
        println("timeLeftInMiliseconds :: $timeLeftInMiliseconds")
        countDown {
            timer = it
            it.start()
        }
    }

    private fun countDown(block: (timer: CountDownTimer) -> Unit) {
        val timer = object : CountDownTimer(timeLeftInMiliseconds, COUNT_DOWN_DEFAULT_INTERVAL) {
            override fun onFinish() {
                Log.d("CountDownTimer", "onFinish")
                if (isCancelable()) cancel()
                else complete()
            }

            override fun onTick(interval: Long) {
//                Log.d("CountDownTimer", "onTick")
                if (isCancelable()) cancel()
                else updateInterval(interval)
            }
        }
        Log.d("isStopped", "$isStopped")
//        Log.d("waitWhileLoading", "$waitWhileLoading")
        Log.d("isPause", "$isPause")
        if (canStart()) block.invoke(timer)
    }

    private fun complete() {
        if (::timerUpdateListener.isInitialized)
            timerUpdateListener.onCompleted()
    }

    private fun updateInterval(interval: Long) {
//        Log.d("StoryTimer", "updateInterval")
        timeLeftInMiliseconds = interval
//        Log.d("StoryTimer", "updateInterval :: time $timeLeftInMiliseconds")
        if (::timerUpdateListener.isInitialized)
            timerUpdateListener.onUpdate(interval)
    }

    private fun isCancelable() = isStopped || isPause || testEnabled //|| waitWhileLoading

    private fun canStart() = !isStopped && !testEnabled //&& !waitWhileLoading

    fun pause() {
        Log.d("StoryTimer", "pause")
        isPause = true
    }

    fun resume() {
        Log.d("StoryTimer", "resume")
        isPause = false
        start()
    }

    fun stop() {
        Log.d("StoryTimer", "stop")
        isStopped = true
        pause()
    }

    fun restart() {
        Log.d("StoryTimer", "restart")
        isPause = false
        isStopped = false
        if (::timer.isInitialized) timer.cancel()
        timeLeftInMiliseconds = delayTime
        start()
    }

    fun isStopped() = isStopped

    fun isPause() = isPause
}