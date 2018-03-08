package com.firetv.infinitekitten

import android.app.Application
import android.content.Context
import android.util.Log
import com.firetv.infinitekitten.api.ApiConstants
import com.firetv.infinitekitten.api.YouTubeApiService
import com.firetv.infinitekitten.api.model.playlist.PlaylistItemList
import com.firetv.infinitekitten.api.model.search.SearchList
import com.firetv.infinitekitten.api.model.video.VideoList
import com.firetv.infinitekitten.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class App : Application() {
    companion object {
        lateinit var instance: App
        val context: Context get() = instance.applicationContext

        val youTubeApiService by lazy {
            YouTubeApiService.create()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //TODO: Code used as sample to be integrated in future code. Delete when no longer required
        val playlistPage = youTubeApiService.getPageForPlaylist(playlistId = ApiConstants.CAT_PLAYLIST)
        playlistPage.enqueue(object : Callback<PlaylistItemList> {
            override fun onFailure(call: Call<PlaylistItemList>?, t: Throwable?) {
                Log.d(Constants.TAG, "getPageForPlaylist failed")
            }

            override fun onResponse(call: Call<PlaylistItemList>?, response: Response<PlaylistItemList>?) {
                Log.d(Constants.TAG, "getPageForPlaylist success")
            }
        })

        val searchPage = youTubeApiService.getPageForSearch(query = ApiConstants.CAT_SEARCH_QUERY, topicId = ApiConstants.CAT_SEARCH_TOPIC_ID)
        searchPage.enqueue(object : Callback<SearchList> {
            override fun onFailure(call: Call<SearchList>?, t: Throwable?) {
                Log.d(Constants.TAG, "getPageForSearch failed")
            }

            override fun onResponse(call: Call<SearchList>?, response: Response<SearchList>?) {
                Log.d(Constants.TAG, "getPageForSearch success")
            }
        })

        val videoInfo = youTubeApiService.getVideoInfo(videoId = "Ks-_Mh1QhMc")
        videoInfo.enqueue(object : Callback<VideoList> {
            override fun onFailure(call: Call<VideoList>?, t: Throwable?) {
                Log.d(Constants.TAG, "getVideoInfo failed")
            }

            override fun onResponse(call: Call<VideoList>?, response: Response<VideoList>?) {
                Log.d(Constants.TAG, "getVideoInfo success")
            }
        })
    }
}