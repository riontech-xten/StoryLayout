package com.xtensolutions.sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.xtensolutions.sample.R
import com.xtensolutions.sample.databinding.RowItemStoryBinding


/**
 * Created by Vaghela Mithun R. on 08/02/21.
 * vaghela.mithun@gmail.com
 */
class StoryAdapter(val context: Context) : PagerAdapter() {
    val inflater: LayoutInflater = LayoutInflater.from(context)

    val images = listOf<Int>(
        R.drawable.appetizer,
        R.drawable.pet_gromming1,
        R.drawable.pet_grooming,
        R.drawable.facial_service,
        R.drawable.one_on_tray_food
    )

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = RowItemStoryBinding.inflate(inflater, container, false)
        binding.image = images.get(position)
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}