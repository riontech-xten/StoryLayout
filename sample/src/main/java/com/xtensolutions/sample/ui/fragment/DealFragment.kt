package com.xtensolutions.sample.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.xtensolutions.storylayout.listener.OnStoryChangeListener
import com.xtensolutions.sample.R
import com.xtensolutions.sample.databinding.FragmentDealBinding
import com.xtensolutions.sample.extension.showToast
import com.xtensolutions.sample.room.model.DealStory
import com.xtensolutions.sample.ui.adapter.StoryPagerAdapter
import com.xtensolutions.sample.ui.viewmodel.DealStoryViewModel
import com.xtensolutions.sample.utils.StoryState
import com.xtensolutions.sample.utils.StoryType
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Vaghela Mithun R. on 14-02-2023 - 12:42.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class DealFragment : Fragment(R.layout.fragment_deal), OnStoryChangeListener {
    private lateinit var binding: FragmentDealBinding
    private val args: DealFragmentArgs by navArgs()
    private val dealViewModel: DealStoryViewModel by viewModels()
    private lateinit var storyPagerAdapter: StoryPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDealBinding.bind(view)
        binding.restaurant = args.restaurant
        getDeals()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getDeals() {
        Log.d(TAG, "getDeals: ${args.restaurant.id}")
        dealViewModel.getRestaurantDeals(args.restaurant.id).observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) {
                noDealAvailable()
                return@observe
            }

            bindStoryPager(it)
        }
    }

    private fun bindStoryPager(deals: List<DealStory>) {
        binding.storyLayout.storyChangeListener = this
        storyPagerAdapter = StoryPagerAdapter(childFragmentManager, lifecycle, deals)
        binding.storyLayout.adapter = storyPagerAdapter
        binding.deal = deals[0]
        binding.noDealFound.root.isVisible = false
    }

    private fun noDealAvailable() {
        binding.noDealFound.root.isVisible = true
        binding.noDealFound.content = getString(R.string.no_deal_available)
        binding.noDealFound.action = "Go back"
        binding.noDealFound.btnNoDataAction.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onNext(position: Int) {
        binding.deal = storyPagerAdapter.getStory(position)
        binding.storyLayout.maxDuration = 15000
    }

    override fun onPrevious(position: Int) {
        binding.deal = storyPagerAdapter.getStory(position)
        binding.storyLayout.maxDuration = 15000
        isVideo(position) {
            storyPagerAdapter.videoStoryFragmentAt(position)?.resetVideo()
        }
    }

    override fun onHold(position: Int) {
        isVideo(position) {
            storyPagerAdapter.videoStoryFragmentAt(position)?.pauseVideo()
        }
    }

    override fun onRelease(position: Int) {
        isVideo(position) {
            storyPagerAdapter.videoStoryFragmentAt(position)?.resumePlayer()
        }
    }

    override fun onRestartStory(position: Int) {
        findNavController().popBackStack()
    }

    override fun onSwipeLeftToRight(position: Int) {
        super.onSwipeLeftToRight(position)
        requireContext().showToast("You swiped Left => Right")
    }

    override fun onSwipeRightToLeft(position: Int) {
        super.onSwipeRightToLeft(position)
        requireContext().showToast("You swiped Right => Left")
    }

    override fun onSwipeUp(position: Int) {
        super.onSwipeUp(position)
        requireContext().showToast("You swiped up")
    }

    override fun onSwipeDown(position: Int) {
        super.onSwipeDown(position)
        requireContext().showToast("You swiped down")
    }

    private fun isVideo(position: Int, block: () -> Unit) {
        if (storyPagerAdapter.getStory(position).storyType == StoryType.VIDEO.value) {
            block.invoke()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.storyLayout.stop()
    }

    fun currentState(state: StoryState) {
        Log.d(TAG, "State => $state")
        when (state) {
            StoryState.START -> binding.storyLayout.start()
            StoryState.RESTART -> binding.storyLayout.reset()
            StoryState.HOLD -> binding.storyLayout.hold()
            StoryState.RELEASE -> binding.storyLayout.release()
            StoryState.STOP -> binding.storyLayout.stop()
            StoryState.COMPLETED -> binding.storyLayout.stop()
            StoryState.BUFFERING -> binding.storyLayout.hold()
            StoryState.ERROR -> binding.storyLayout.stop()
        }
    }

    companion object {
        const val TAG = "DealFragment"
    }
}