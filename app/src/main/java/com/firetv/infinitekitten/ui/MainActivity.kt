package com.firetv.infinitekitten.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.animation.AnimationUtils
import com.firetv.infinitekitten.App
import com.firetv.infinitekitten.R
import com.firetv.infinitekitten.api.ApiConstants
import com.firetv.infinitekitten.data.YouTubeMediaItem
import com.firetv.infinitekitten.playlist.YouTubeMediaItemFetcher
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        videosMeButton.hasFocus()
        buildPlaylist()
    }

    private fun setupUI() {
        videosMeButton.setOnFocusChangeListener { v, hasFocus ->
            onFocusChangeListener(v, hasFocus)
        }

        videosHumanButton.setOnFocusChangeListener { v, hasFocus ->
            onFocusChangeListener(v, hasFocus)
        }
    }

    private fun onFocusChangeListener(v: View, hasFocus: Boolean) {
        val anim = AnimationUtils.loadAnimation(this,
                if (hasFocus) R.anim.scale_in else R.anim.scale_out)
        v.startAnimation(anim)
        anim.fillAfter = true
    }

    private fun buildPlaylist() {
        val youtubeMediaItemFetcher = YouTubeMediaItemFetcher(
                context = App.context,
                playlistId = ApiConstants.CAT_PLAYLIST
        )

        val intent = Intent(this, VideoPlayerActivity::class.java)

        youtubeMediaItemFetcher.fetch(object : YouTubeMediaItemFetcher.YouTubeMediaItemFetcherCallback {
            override fun onSuccess(result: List<YouTubeMediaItem>, nextPageToken: String) {

                val playlistManager = App.playlistManager
                playlistManager.setParameters(result, 0)
                startActivity(intent)
            }

            override fun onFailure() {
                //TODO: Handle failure.
            }
        })
    }
}
