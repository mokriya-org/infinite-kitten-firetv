package com.firetv.infinitekitten

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.firetv.infinitekitten.api.flickr.Flickr
import com.firetv.infinitekitten.manager.PlaylistManager
import com.firetv.infinitekitten.utils.EventTrackerUtil
import io.fabric.sdk.android.Fabric
import com.firetv.infinitekitten.api.flickr.model.SearchResponse as FlickrSearchResponse
import com.firetv.infinitekitten.api.youtube.model.search.SearchResponse as YoutubeSearchResponse


class App : Application() {
    companion object {
        lateinit var instance: App
        val context: Context get() = instance.applicationContext
        lateinit var playlistManager: PlaylistManager
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (!BuildConfig.DEBUG) {
            Fabric.with(instance, Crashlytics())
            EventTrackerUtil.setupTracker()
        }

        playlistManager = PlaylistManager(instance)
        Flickr.initUrls()
    }
}