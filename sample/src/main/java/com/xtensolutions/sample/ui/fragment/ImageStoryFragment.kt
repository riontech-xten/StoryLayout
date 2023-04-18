package com.xtensolutions.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.xtensolutions.sample.extension.loadBitmap
import com.xtensolutions.sample.room.model.DealStory
import com.xtensolutions.sample.utils.StoryState
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Vaghela Mithun R. on 12/06/21.
 * vaghela.mithun@gmail.com
 */
@AndroidEntryPoint
class ImageStoryFragment : StoryFragment() {

    companion object {
        fun newInstance(story: DealStory, position: Int): Fragment {
            return ImageStoryFragment().apply {
                arguments = args(story, position)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isStateListenerInit()) dealFragment.currentState(StoryState.HOLD)
        dataBinding.storyProgress.isVisible = false
        loadImage()
    }

    override fun onResume() {
        super.onResume()
        if (isStateListenerInit()) dealFragment.currentState(StoryState.RELEASE)
    }

    private fun loadImage() {
        dataBinding.deal?.let {
            dataBinding.storyImage.loadBitmap(it.image) {
                requireActivity().runOnUiThread {
                    if (isStateListenerInit()) dealFragment.currentState(StoryState.RELEASE)
                }
            }
        }
    }
}