package com.xtensolutions.storylayout.timer

import android.os.CountDownTimer
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit


/**
 * Created by Vaghela Mithun R. on 10/02/21.
 * vaghela.mithun@gmail.com
 */
class StoryTimer(private val delayTime: Long) {
    private var isStopped: Boolean = false
    private var isPause: Boolean = false

    private var timeLeftInMiliseconds = delayTime
    lateinit var timerUpdateListener: OnTimerUpdateListener

    interface OnTimerUpdateListener {
        fun onCompleted()
        fun onUpdate(interval: Long)
    }

    fun start() {
        if (!isStopped) {
            object : CountDownTimer(timeLeftInMiliseconds, 5) {
                override fun onFinish() {
                    if (isStopped || isPause) {
                        cancel()
                    } else {
                        timerUpdateListener.onCompleted()
                    }
                }

                override fun onTick(interval: Long) {
                    if (isStopped || isPause) {
                        cancel()
                    } else {
                        Log.d("StoryTimer", "onTick")
                        timeLeftInMiliseconds = interval
                        timerUpdateListener.onUpdate(interval)
                    }
                }
            }.start()
        }
    }

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
        Log.d("StoryTimer", "start")
        isPause = false
        isStopped = false
        timeLeftInMiliseconds = delayTime
        start()
    }
}