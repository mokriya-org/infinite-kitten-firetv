package com.firetv.infinitekitten.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.devbrackets.android.exomedia.listener.VideoControlsSeekListener
import com.devbrackets.android.playlistcore.data.PlaybackState
import com.devbrackets.android.playlistcore.listener.PlaylistListener
import com.firetv.infinitekitten.App
import com.firetv.infinitekitten.R
import com.firetv.infinitekitten.manager.PlaylistManager
import com.firetv.infinitekitten.model.VideoPlaylistItem
import com.firetv.infinitekitten.playlist.VideoMediaPlayerApi
import com.firetv.infinitekitten.playlist.YouTubeMediaItemFetcher
import kotlinx.android.synthetic.main.activity_video_player.*

/**
 * Created by diogobrito on 09/03/2018.
 */
class VideoPlayerActivity : FragmentActivity(), VideoControlsSeekListener {

    private lateinit var videoMediaPlayerApi: VideoMediaPlayerApi
    private val playlistManager: PlaylistManager = App.playlistManager
    private var nextPlayList: List<VideoPlaylistItem> = mutableListOf()
    private var playNextPlayList: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        setupPlayer()
    }

    override fun onStop() {
        super.onStop()
        playlistManager.removeVideoMediaPlayerApi(videoMediaPlayerApi)
        playlistManager.unRegisterPlaylistListener(playListListener)
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

        videoMediaPlayerApi = VideoMediaPlayerApi(videoView, playlistManager.playlistId)
        videoView.setOnCompletionListener {
            if(playNextPlayList){
                playNextPlaylist()
            }
        }
        playlistManager.addVideoMediaPlayerApi(videoMediaPlayerApi)
        playlistManager.registerPlaylistListener(playListListener)
        playlistManager.play(0, false)
    }

    val playListListener = object : PlaylistListener<VideoPlaylistItem> {
        override fun onPlaybackStateChanged(playbackState: PlaybackState): Boolean {
            return false
        }

        override fun onPlaylistItemChanged(currentItem: VideoPlaylistItem?, hasNext: Boolean, hasPrevious: Boolean): Boolean {
            playNextPlayList = !hasNext
            if (!hasNext && nextPlayList.isEmpty()) {
                loadNextPlaylist()
            }
            return false
        }
    }

    private fun loadNextPlaylist() {
        val youtubeMediaItemFetcher = YouTubeMediaItemFetcher(
                context = App.context,
                playlistId = playlistManager.playlistId,
                pageToken = playlistManager.nextPageToken
        )

        youtubeMediaItemFetcher.fetch(object : YouTubeMediaItemFetcher.YouTubeMediaItemFetcherCallback {
            override fun onSuccess(result: List<VideoPlaylistItem>, nextPageToken: String) {
                val playlistManager = App.playlistManager
                nextPlayList = result
                if(nextPlayList.isNotEmpty()){
                    playlistManager.nextPageToken = nextPageToken
                }
            }

            override fun onFailure() {
            }
        })
    }

    private fun playNextPlaylist() {
        playlistManager.setParameters(nextPlayList.toList(), 0)
        nextPlayList = mutableListOf()
        playlistManager.play(0, false)
    }
}
