package com.firetv.infinitekitten.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.animation.AnimationUtils
import com.firetv.infinitekitten.App
import com.firetv.infinitekitten.R
import com.firetv.infinitekitten.api.ApiConstants
import com.firetv.infinitekitten.model.VideoPlaylistItem
import com.firetv.infinitekitten.playlist.YouTubeMediaItemFetcher
import com.firetv.infinitekitten.ui.fragment.LoadingFragment
import com.firetv.infinitekitten.utils.EventTrackerUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    private var playVideosOnLoad = false

    //region FragmentActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        hideLoading()
    }

    override fun onStop() {
        super.onStop()
        playVideosOnLoad = false
    }

    override fun onBackPressed() {
        if (container.visibility == View.VISIBLE) {
            hideLoading()
        } else {
            super.onBackPressed()
        }
    }
    //endregion

    //region Private
    private fun setupUI() {
        videosMeButton.setOnFocusChangeListener { v, hasFocus ->
            onFocusChangeListener(v, hasFocus)
        }

        videosHumanButton.setOnFocusChangeListener { v, hasFocus ->
            onFocusChangeListener(v, hasFocus)
        }

        videosMeButton.setOnClickListener {
            EventTrackerUtil.trackEvent(EventTrackerUtil.EVENT_CAT_VIDEOS_BUTTON_SELECTED)
            showPlaylist(ApiConstants.CAT_PLAYLIST)
        }

        videosHumanButton.setOnClickListener {
            EventTrackerUtil.trackEvent(EventTrackerUtil.EVENT_HUMAN_VIDEOS_BUTTON_SELECTED)
            showPlaylist(ApiConstants.HUMAN_PLAYLIST)
        }
    }

    private fun showPlaylist(playlistId: String) {
        showLoading()
        buildPlaylist(playlistId)
    }

    private fun onFocusChangeListener(v: View, hasFocus: Boolean) {
        val anim = AnimationUtils.loadAnimation(this,
                if (hasFocus) R.anim.scale_in else R.anim.scale_out)
        v.startAnimation(anim)
        anim.fillAfter = true
    }

    private fun showLoading() {
        playVideosOnLoad = true
        container.visibility = View.VISIBLE
        (supportFragmentManager.findFragmentById(R.id.loadingFragment) as LoadingFragment).setupUI()
    }

    private fun hideLoading() {
        playVideosOnLoad = false
        container.visibility = View.GONE
    }


    private fun buildPlaylist(playlistId: String) {
        val youtubeMediaItemFetcher = YouTubeMediaItemFetcher(
                context = App.context,
                playlistId = playlistId
        )

        val intent = Intent(this, VideoPlayerActivity::class.java)
        youtubeMediaItemFetcher.fetch(object : YouTubeMediaItemFetcher.YouTubeMediaItemFetcherCallback {
            override fun onSuccess(result: List<VideoPlaylistItem>, nextPageToken: String) {
                if (!playVideosOnLoad || isFinishing) {
                    return
                }

                val playlistManager = App.playlistManager
                playlistManager.setParameters(result, 0)
                playlistManager.playlistId = playlistId
                playlistManager.nextPageToken = nextPageToken
                startActivity(intent)
            }

            override fun onFailure() {
                hideLoading()
            }
        })
    }
    //endregion
}
