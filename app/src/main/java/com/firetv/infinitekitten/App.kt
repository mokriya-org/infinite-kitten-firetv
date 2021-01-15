package com.firetv.infinitekitten

import android.app.Application
import android.content.Context
import com.firetv.infinitekitten.api.flickr.Flickr
import com.firetv.infinitekitten.manager.PlaylistManager
import com.firetv.infinitekitten.utils.EventTrackerUtil


class App : Application() {
    companion object {
        lateinit var instance: App
        val context: Context get() = instance.applicationContext
        lateinit var playlistManager: PlaylistManager
    }

    override fun onCreate() {
        instance = this

        super.onCreate()

        if (!BuildConfig.DEBUG) {
            EventTrackerUtil.setupTracker()
        }

        playlistManager = PlaylistManager(instance)
        Flickr.initUrls()
    }
}