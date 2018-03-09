package com.firetv.infinitekitten.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.bumptech.glide.Glide
import com.firetv.infinitekitten.R
import com.firetv.infinitekitten.api.flickr.Flickr
import kotlinx.android.synthetic.main.fragment_loading.*

/**
 * Created by dileepan on 09/03/18.
 */
class LoadingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()

    }

    private fun setupUI() {
        val bgImageUrl = Flickr.getFlickPhotoUrl()
        Glide.with(context).load(bgImageUrl).into(background)

        loadingIndicator.startAnimation(LoadingIndicatorAnimation(loadingIndicator).apply {
            duration = 500
            repeatCount = Animation.INFINITE
        })

        val gifName = loadingIndicator.getSelectedGif()
        val yourData = "<html style=\"margin: 0;\">\n" +
                "    <body style=\"margin: 0;\">\n" +
                "    <img src=" + gifName + " style=\"width: 100%; height: 100%\" />\n" +
                "    </body>\n" +
                "    </html>"

        loadingGif.setBackgroundColor(Color.TRANSPARENT)
        loadingGif.settings.allowFileAccess = true
        loadingGif.loadDataWithBaseURL("file:///android_asset/gifs/", yourData, "text/html", "utf-8", null)
    }
}