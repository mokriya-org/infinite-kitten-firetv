package com.firetv.infinitekitten

import android.app.Application
import android.content.Context
import android.util.Log
import com.firetv.infinitekitten.api.flickr.FlickrApiService
import com.firetv.infinitekitten.api.flickr.model.PhotoSizesResponse
import com.firetv.infinitekitten.api.youtube.YouTubeApiService
import com.firetv.infinitekitten.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.firetv.infinitekitten.api.flickr.model.SearchResponse as FlickrSearchList
import com.firetv.infinitekitten.api.youtube.model.search.SearchResponse as YoutubeSearchList

class App : Application() {
    companion object {
        lateinit var instance: App
        val context: Context get() = instance.applicationContext

        val youTubeApiService by lazy {
            YouTubeApiService.create()
        }

        val flickrApiService by lazy {
            FlickrApiService.create()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //TODO: Code used as sample to be integrated in future code. Delete when no longer required
        /*    val playlistPage = youTubeApiService.getPageForPlaylist(playlistId = ApiConstants.CAT_PLAYLIST)
            playlistPage.enqueue(object : Callback<PlaylistItemList> {
                override fun onFailure(call: Call<PlaylistItemList>?, t: Throwable?) {
                    Log.d(Constants.TAG, "getPageForPlaylist failed")
                }

                override fun onResponse(call: Call<PlaylistItemList>?, response: Response<PlaylistItemList>?) {
                    Log.d(Constants.TAG, "getPageForPlaylist success")
                }
            })
            val searchPage = youTubeApiService.getPageForSearch(query = ApiConstants.CAT_SEARCH_QUERY, topicId = ApiConstants.CAT_SEARCH_TOPIC_ID)
            searchPage.enqueue(object : Callback<YoutubeSearchList> {
                override fun onFailure(call: Call<YoutubeSearchList>?, t: Throwable?) {
                    Log.d(Constants.TAG, "getPageForSearch failed")
                }

                override fun onResponse(call: Call<YoutubeSearchList>?, response: Response<YoutubeSearchList>?) {
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
            })*/


        val photos = flickrApiService.getPhotos(query = "kitten", page = 1)
        photos.enqueue(object : Callback<FlickrSearchList> {
            override fun onFailure(call: Call<FlickrSearchList>?, t: Throwable?) {
                Log.d(Constants.TAG, "getPhotos failed")
            }

            override fun onResponse(call: Call<FlickrSearchList>?, response: Response<FlickrSearchList>?) {
                Log.d(Constants.TAG, "getPhotos success")
            }

        })

        val photoSizes = flickrApiService.getPhotosSizes(photoId = "39801607805")
        photoSizes.enqueue(object : Callback<PhotoSizesResponse> {
            override fun onFailure(call: Call<PhotoSizesResponse>?, t: Throwable?) {
                Log.d(Constants.TAG, "getPhotosSizes failed")
            }

            override fun onResponse(call: Call<PhotoSizesResponse>?, response: Response<PhotoSizesResponse>?) {
                Log.d(Constants.TAG, "getPhotosSizes success")
            }
        })
    }
}