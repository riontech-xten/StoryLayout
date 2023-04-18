package com.xtensolutions.sample.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.video.VideoSize
import com.xtensolutions.sample.extension.screenRatio
import com.xtensolutions.sample.room.model.DealStory
import com.xtensolutions.sample.utils.StoryState
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Vaghela Mithun R. on 12/06/21.
 * vaghela.mithun@gmail.com
 */
@AndroidEntryPoint
class VideoStoryFragment : StoryFragment(), Player.Listener {
    private lateinit var exoplayer: ExoPlayer
    private var isStateAlreadyReady = false


    companion object {
        fun newInstance(story: DealStory, position: Int): Fragment {
            return VideoStoryFragment().apply {
                arguments = args(story, position)
            }
        }

        private const val TAG = "VideoStoryFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlayer()
    }

    private fun initPlayer() {
        dataBinding.deal?.let {
            exoplayer = ExoPlayer.Builder(requireContext()).build()
            dataBinding.videoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            dataBinding.videoPlayerView.player = exoplayer
            val mediaItem = MediaItem.fromUri(it.image)
            exoplayer.addMediaItem(mediaItem)
            exoplayer.prepare()
            exoplayer.addListener(this)
        }
    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        super.onVideoSizeChanged(videoSize)
        dataBinding.aspectRationFrame.setAspectRatio(requireContext().screenRatio())
        dataBinding.aspectRationFrame.requestLayout()
    }

    override fun onRenderedFirstFrame() {
        super.onRenderedFirstFrame()
        dataBinding.aspectRationFrame.setAspectRatio(requireContext().screenRatio())
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        playVideo()
    }

    override fun onPlaybackStateChanged(state: Int) {
        super.onPlaybackStateChanged(state)
        var storyState = StoryState.HOLD
        when (state) {

            Player.STATE_BUFFERING -> storyState = StoryState.BUFFERING
            Player.STATE_READY -> {
                dataBinding.storyProgress.isVisible = false
                storyState = StoryState.RELEASE
            }

            Player.STATE_ENDED -> {
                dataBinding.storyProgress.isVisible = false
                storyState = StoryState.COMPLETED
                pauseVideo()
            }

            Player.STATE_IDLE -> Log.d("VideoStoryFragment", "Player.STATE_IDLE")
        }

        if (isStateListenerInit()) {
            dealFragment.currentState(storyState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("$TAG, onDestroyView($position)")
        isStateAlreadyReady = false
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        println("$TAG, onPause($position)")
        pauseVideo()
    }

    override fun onResume() {
        super.onResume()
        println("$TAG, onResume($position)")
//        isStateAlreadyReady = false
        playVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("$TAG, onDestroy($position)")
        releasePlayer()
    }

    override fun onDetach() {
        super.onDetach()
        println("$TAG, onDetach($position)")
        releasePlayer()
    }

    private fun playVideo() {
        if (!::exoplayer.isInitialized || dataBinding.videoPlayerView.player == null) {
            initPlayer()
        } else {
            exoplayer.seekToDefaultPosition()
            exoplayer.play()
        }
    }

    fun pauseVideo() {
        if (::exoplayer.isInitialized) {
            exoplayer.playWhenReady = false
            exoplayer.pause()
        }
    }

    private fun releasePlayer() {
        if (::exoplayer.isInitialized) {
            exoplayer.pause()
            exoplayer.release()
        }
    }

    fun resumePlayer() {
        if (::exoplayer.isInitialized) {
            exoplayer.playWhenReady = true
            exoplayer.play()
        }
    }

    fun resetVideo() {
        if (::exoplayer.isInitialized && exoplayer.isPlaying) {
            exoplayer.seekTo(0)
            exoplayer.playWhenReady = true
            exoplayer.play()
        }
    }
}