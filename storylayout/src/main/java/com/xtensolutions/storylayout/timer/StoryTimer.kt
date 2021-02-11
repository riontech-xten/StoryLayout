package com.xtensolutions.storylayout.timer

import android.os.CountDownTimer


/**
 * Created by Vaghela Mithun R. on 10/02/21.
 * vaghela.mithun@gmail.com
 */
class StoryTimer(private val delayTime: Long) {
    private lateinit var timer: CountDownTimer
    private var isRunning: Boolean = false;
    private var isStopped: Boolean = false
    private var timeLeftInMiliseconds = delayTime
    private lateinit var timerUpdateListener: OnTimerUpdateListener

    constructor(delayTime: Long, timerUpdateListener: OnTimerUpdateListener) : this(delayTime) {
        this.timerUpdateListener = timerUpdateListener
    }

    interface OnTimerUpdateListener {
        fun onCompleted()
        fun onUpdate(interval: Long)
    }

    fun start() {
        if (!isStopped) {
            timer = object : CountDownTimer(timeLeftInMiliseconds, 10) {
                override fun onFinish() {
                    timerUpdateListener.onCompleted()
                }

                override fun onTick(interval: Long) {
                    timeLeftInMiliseconds = interval
                    timerUpdateListener.onUpdate(interval)
                }
            }.start()

            isRunning = true
        }
    }

    fun reset() {
        timeLeftInMiliseconds = delayTime
        start()
    }

    fun pause() {
        if (isRunning) {
            timer.cancel()
            isRunning = false
        }
    }

    fun stop() {
        pause()
        isStopped = true
    }

//    private fun startCounter() {
//        val f = CoroutineScope(Job() + Dispatchers.Main).launch {
//            flowInterval(1000, 100)
//                .flowOn(Dispatchers.IO)
//                .onEach { println("on each::$it") }
//                .collect {
//                    println("collected::${TimeUnit.MILLISECONDS.toSeconds(it)}")
//                }
//        }
//
//        f.cancel()
//    }
//
//    fun flowInterval(delayMillis: Long, initialDelayMillis: Long = 0L) = flow {
//        require(delayMillis > 0) { "delayMillis must be positive" }
//        require(initialDelayMillis >= 0) { "initialDelayMillis cannot be negative" }
//        if (initialDelayMillis > 0) {
//            delay(initialDelayMillis)
//        }
////        emit(System.currentTimeMillis())
//        while (true) {
//            delay(delayMillis)
//            emit(System.currentTimeMillis())
//        }
//    }.cancellable().buffer()
}