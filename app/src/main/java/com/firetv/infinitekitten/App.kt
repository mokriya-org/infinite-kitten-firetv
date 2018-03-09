package com.firetv.infinitekitten

import android.app.Application
import android.content.Context
import com.firetv.infinitekitten.manager.PlaylistManager
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
        playlistManager = PlaylistManager(this)
    }
}