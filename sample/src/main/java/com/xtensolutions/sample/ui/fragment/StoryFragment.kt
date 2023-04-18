package com.xtensolutions.sample.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xtensolutions.sample.R
import com.xtensolutions.sample.databinding.RowItemStoryBinding
import com.xtensolutions.sample.room.model.DealStory
import com.xtensolutions.sample.utils.StoryState


/**
 * Created by Vaghela Mithun R. on 17/06/21.
 * vaghela.mithun@gmail.com
 */
abstract class StoryFragment : Fragment(R.layout.row_item_story) {
    protected var position = -1
    protected lateinit var dataBinding: RowItemStoryBinding
    lateinit var dealFragment: DealFragment

    companion object {
        const val ARG_DEAL = "deal"
        const val ARG_POSITION = "story_index"

        fun args(story: DealStory, position: Int) = Bundle().apply {
            putSerializable(ARG_DEAL, story)
            putInt(ARG_POSITION, position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (parentFragment is DealFragment) {
            dealFragment = parentFragment as DealFragment
            dealFragment.currentState(StoryState.HOLD)
        }
        dataBinding = RowItemStoryBinding.bind(view)
        getStoryData()?.let {
            position = requireArguments().getInt(ARG_POSITION)
            dataBinding.deal = it
        }
    }

    fun isStateListenerInit() = ::dealFragment.isInitialized

//    fun isVideoStory(): Boolean = dataBinding.deal?.storyType == StoryType.VIDEO.value

    private fun getStoryData() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        requireArguments().getSerializable(ARG_DEAL, DealStory::class.java)
    } else {
        requireArguments().getSerializable(ARG_DEAL) as? DealStory
    }
}