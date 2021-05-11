package com.xtensolutions.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xtensolutions.sample.adapter.StoryAdapter
import com.xtensolutions.storylayout.StoryLayout
import com.xtensolutions.storylayout.timer.StoryTimer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val storyLayout = findViewById<StoryLayout>(R.id.storyLayout)
        storyLayout.adapter = StoryAdapter(this)
        StoryTimer(1000).channelTicker()
//        GlobalScope.launch {
//            channelTest()
//        }
    }

    private suspend fun channelTest() {
        val channel = Channel<Int>()
        GlobalScope.launch {
            // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
            for (x in 1..5) channel.send(x * x)
        }

// here we print five received integers:
        repeat(5) { println(channel.receive()) }
        println("Done!")
    }
}