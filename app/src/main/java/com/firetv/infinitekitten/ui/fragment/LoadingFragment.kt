package com.firetv.infinitekitten.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.bumptech.glide.Glide
import com.firetv.infinitekitten.R
import com.firetv.infinitekitten.api.flickr.Flickr
import com.firetv.infinitekitten.ui.animation.LoadingIndicatorAnimation
import kotlinx.android.synthetic.main.fragment_loading.*

/**
 * Created by dileepan on 09/03/18.
 */
class LoadingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()
    }

    fun setupUI() {
        val bgImageUrl = Flickr.getFlickPhotoUrl()
        if (bgImageUrl != null) Glide.with(context).load(bgImageUrl).dontAnimate().into(background)

        loadingIndicator.startAnimation(LoadingIndicatorAnimation(loadingIndicator).apply {
            duration = 500
            repeatCount = Animation.INFINITE
        })

        loadingIndicator.init()
        val gifName = loadingIndicator.getSelectedGif()
        val stylizedGif = "<html style=\"margin: 0;\">\n" +
                "    <body style=\"margin: 0;\">\n" +
                "    <img src=" + gifName + " style=\"width: 100%; height: 100%\" />\n" +
                "    </body>\n" +
                "    </html>"

        loadingGif.setBackgroundColor(Color.TRANSPARENT)
        loadingGif.settings.allowFileAccess = true
        Handler(Looper.getMainLooper()).post {
            loadingGif.loadDataWithBaseURL("file:///android_asset/gifs/", stylizedGif, "text/html", "utf-8", null)
        }
    }
}