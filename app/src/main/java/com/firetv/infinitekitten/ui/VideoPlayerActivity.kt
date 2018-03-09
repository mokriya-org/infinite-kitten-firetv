package com.firetv.infinitekitten.ui

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.firetv.infinitekitten.R
import com.firetv.infinitekitten.utils.Constants
import kotlinx.android.synthetic.main.activity_video_player.*

/**
 * Created by diogobrito on 09/03/2018.
 */
class VideoPlayerActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        setupUI()
    }

    override fun onStart() {
        super.onStart()
        val youtubeExtractor: YouTubeExtractor = object : YouTubeExtractor(this.applicationContext) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
                ytFiles?.let {
                    val ytFile = it.get(22)

                    Log.d(Constants.TAG, "getVideoInfo failed")
                    videoView.setVideoURI(Uri.parse(ytFile.url))
                }
            }
        }
        youtubeExtractor.extract("https://www.youtube.com/watch?v=Qgvip8ISSF0", true, true)
    }


    private fun setupUI() {
        videoView.setOnPreparedListener {
            videoView.start()
        }
    }
}