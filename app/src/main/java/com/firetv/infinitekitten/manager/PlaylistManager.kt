package com.firetv.infinitekitten.manager

import android.app.Application
import com.devbrackets.android.exomedia.listener.VideoControlsButtonListener
import com.devbrackets.android.playlistcore.manager.ListPlaylistManager
import com.firetv.infinitekitten.data.VideoPlaylistItem
import com.firetv.infinitekitten.playlist.VideoMediaPlayerApi
import com.firetv.infinitekitten.service.MediaService

/**
 * Created by diogobrito on 09/03/2018.
 */


class PlaylistManager(application: Application) : ListPlaylistManager<VideoPlaylistItem>(application, MediaService::class.java) {

    fun addVideoMediaPlayerApi(videoMediaPlayerApi: VideoMediaPlayerApi) {
        mediaPlayers.add(videoMediaPlayerApi)
        updateVideoControls(videoMediaPlayerApi)
        registerPlaylistListener(videoMediaPlayerApi)
    }


    fun removeVideoMediaPlayerApi(videoMediaPlayerApi: VideoMediaPlayerApi) {
        val controls = videoMediaPlayerApi.videoView.videoControls
        controls?.setButtonListener(null)

        unRegisterPlaylistListener(videoMediaPlayerApi)
        mediaPlayers.remove(videoMediaPlayerApi)
    }

    private fun updateVideoControls(videoMediaPlayerApi: VideoMediaPlayerApi) {
        val videoControls = videoMediaPlayerApi.videoView.videoControls
        videoControls?.setPreviousButtonRemoved(false)
        videoControls?.setNextButtonRemoved(false)
        videoControls?.setButtonListener(ControlsListener())
    }

    /**
     * An implementation of the [VideoControlsButtonListener] that provides
     * integration with the playlist service.
     */
    private inner class ControlsListener : VideoControlsButtonListener {
        override fun onPlayPauseClicked(): Boolean {
            invokePausePlay()
            return true
        }

        override fun onPreviousClicked(): Boolean {
            invokePrevious()
            return false
        }

        override fun onNextClicked(): Boolean {
            invokeNext()
            return false
        }

        override fun onRewindClicked(): Boolean {
            return false
        }

        override fun onFastForwardClicked(): Boolean {
            return false
        }
    }
}