package com.xtensolutions.sample.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xtensolutions.sample.room.model.DealStory
import com.xtensolutions.sample.ui.fragment.ImageStoryFragment
import com.xtensolutions.sample.ui.fragment.VideoStoryFragment
import com.xtensolutions.sample.utils.StoryType


/**
 * Created by Vaghela Mithun R. on 12/06/21.
 * vaghela.mithun@gmail.com
 */
class StoryPagerAdapter(
    private val manager: FragmentManager,
    lifecycle: Lifecycle,
    private val deals: List<DealStory>
) : FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int {
        return deals.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (deals[position].storyType == StoryType.VIDEO.value)
            VideoStoryFragment.newInstance(deals[position], position)
        else ImageStoryFragment.newInstance(deals[position], position)
    }

    fun getStory(position: Int) = deals[position]

    fun videoStoryFragmentAt(position: Int): VideoStoryFragment? {
        val fragment = manager.findFragmentByTag("f$position")
        if (fragment is VideoStoryFragment)
            return fragment
        return null
    }
}