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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        hideLoading()
    }

    private fun setupUI() {
        videosMeButton.setOnFocusChangeListener { v, hasFocus ->
            onFocusChangeListener(v, hasFocus)
        }

        videosHumanButton.setOnFocusChangeListener { v, hasFocus ->
            onFocusChangeListener(v, hasFocus)
        }

        videosMeButton.setOnClickListener {
            showLoading()
            buildPlaylist(ApiConstants.CAT_PLAYLIST)
        }

        videosHumanButton.setOnClickListener {
            showLoading()
            buildPlaylist(ApiConstants.HUMAN_PLAYLIST)
        }
    }

    private fun showLoading() {
        container.visibility = View.VISIBLE
        (supportFragmentManager.findFragmentById(R.id.loadingFragment) as LoadingFragment).setupUI()
    }

    private fun hideLoading() {
        container.visibility = View.GONE
    }

    private fun onFocusChangeListener(v: View, hasFocus: Boolean) {
        val anim = AnimationUtils.loadAnimation(this,
                if (hasFocus) R.anim.scale_in else R.anim.scale_out)
        v.startAnimation(anim)
        anim.fillAfter = true
    }

    override fun onStart() {
        super.onStart()
        isRunning = true
    }

    override fun onStop() {
        super.onStop()
        isRunning = false
    }

    private fun buildPlaylist(playlistId: String) {
        val youtubeMediaItemFetcher = YouTubeMediaItemFetcher(
                context = App.context,
                playlistId = playlistId
        )

        val intent = Intent(this, VideoPlayerActivity::class.java)
        youtubeMediaItemFetcher.fetch(object : YouTubeMediaItemFetcher.YouTubeMediaItemFetcherCallback {
            override fun onSuccess(result: List<VideoPlaylistItem>, nextPageToken: String) {
                if (!isRunning || isFinishing) {
                    return
                }

                val playlistManager = App.playlistManager
                playlistManager.setParameters(result, 0)
                startActivity(intent)
            }

            override fun onFailure() {
                hideLoading()
            }
        })
    }
}