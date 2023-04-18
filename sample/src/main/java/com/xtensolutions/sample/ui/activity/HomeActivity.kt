package com.xtensolutions.sample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xtensolutions.sample.R
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Vaghela Mithun R. on 10-02-2023 - 20:31.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}