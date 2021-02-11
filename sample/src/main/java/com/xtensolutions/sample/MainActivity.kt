package com.xtensolutions.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xtensolutions.sample.adapter.StoryAdapter
import com.xtensolutions.storylayout.StoryLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val storyLayout = findViewById<StoryLayout>(R.id.storyLayout)
        storyLayout.adapter = StoryAdapter(this)
    }
}