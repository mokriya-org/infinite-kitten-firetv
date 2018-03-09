package com.firetv.infinitekitten.ui

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.devbrackets.android.exomedia.listener.VideoControlsSeekListener
import com.firetv.infinitekitten.App
import com.firetv.infinitekitten.R
import com.firetv.infinitekitten.manager.PlaylistManager
import com.firetv.infinitekitten.playlist.VideoMediaPlayerApi
import kotlinx.android.synthetic.main.activity_video_player.*

/**
 * Created by diogobrito on 09/03/2018.
 */
class VideoPlayerActivity : FragmentActivity(), VideoControlsSeekListener {

    private lateinit var videoMediaPlayerApi: VideoMediaPlayerApi
    private val playlistManager: PlaylistManager = App.playlistManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        setupPlayer()
    }


    override fun onStop() {
        super.onStop()
        playlistManager.removeVideoMediaPlayerApi(videoMediaPlayerApi)
        playlistManager.invokeStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        playlistManager.invokeStop()
    }

    override fun onSeekStarted(): Boolean {
        playlistManager.invokeSeekStarted()
        return true
    }

    override fun onSeekEnded(seekTime: Long): Boolean {
        playlistManager.invokeSeekEnded(seekTime)
        return true
    }

    private fun setupPlayer() {
        videoView.setHandleAudioFocus(false)
        videoView.videoControls?.setSeekListener(this)

        videoMediaPlayerApi = VideoMediaPlayerApi(videoView)
        playlistManager.addVideoMediaPlayerApi(videoMediaPlayerApi)
        playlistManager.play(0, false)
    }
}