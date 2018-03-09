package com.firetv.infinitekitten

import android.app.Application
import android.content.Context
import android.util.Log
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.firetv.infinitekitten.api.ApiConstants
import com.firetv.infinitekitten.api.flickr.FlickrApiService
import com.firetv.infinitekitten.api.flickr.model.PhotoSizesResponse
import com.firetv.infinitekitten.api.youtube.YouTubeApiService
import com.firetv.infinitekitten.api.youtube.model.playlist.PlaylistItemsResponse
import com.firetv.infinitekitten.api.youtube.model.video.VideosResponse
import com.firetv.infinitekitten.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.firetv.infinitekitten.api.flickr.model.SearchResponse as FlickrSearchResponse
import com.firetv.infinitekitten.api.youtube.model.search.SearchResponse as YoutubeSearchResponse

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
        val playlistPage = youTubeApiService.getPageForPlaylist(playlistId = ApiConstants.CAT_PLAYLIST)
        playlistPage.enqueue(object : Callback<PlaylistItemsResponse> {
            override fun onFailure(call: Call<PlaylistItemsResponse>?, t: Throwable?) {
                Log.d(Constants.TAG, "getPageForPlaylist failed")
            }

            override fun onResponse(call: Call<PlaylistItemsResponse>?, response: Response<PlaylistItemsResponse>?) {
                Log.d(Constants.TAG, "getPageForPlaylist success")
            }
        })
        val searchPage = youTubeApiService.getPageForSearch(query = ApiConstants.CAT_SEARCH_QUERY, topicId = ApiConstants.CAT_SEARCH_TOPIC_ID)
        searchPage.enqueue(object : Callback<YoutubeSearchResponse> {
            override fun onFailure(call: Call<YoutubeSearchResponse>?, t: Throwable?) {
                Log.d(Constants.TAG, "getPageForSearch failed")
            }

            override fun onResponse(call: Call<YoutubeSearchResponse>?, response: Response<YoutubeSearchResponse>?) {
                Log.d(Constants.TAG, "getPageForSearch success")
            }
        })

        val videoInfo = youTubeApiService.getVideoInfo(videoId = "Ks-_Mh1QhMc")
        videoInfo.enqueue(object : Callback<VideosResponse> {
            override fun onFailure(call: Call<VideosResponse>?, t: Throwable?) {
                Log.d(Constants.TAG, "getVideoInfo failed")
            }

            override fun onResponse(call: Call<VideosResponse>?, response: Response<VideosResponse>?) {
                Log.d(Constants.TAG, "getVideoInfo success")
            }
        })


        val photos = flickrApiService.getPhotos(query = "kitten", page = 1)
        photos.enqueue(object : Callback<FlickrSearchResponse> {
            override fun onFailure(call: Call<FlickrSearchResponse>?, t: Throwable?) {
                Log.d(Constants.TAG, "getPhotos failed")
            }

            override fun onResponse(call: Call<FlickrSearchResponse>?, response: Response<FlickrSearchResponse>?) {
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

        val youtubeExtractor: YouTubeExtractor = object : YouTubeExtractor(this.applicationContext) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
                ytFiles?.let {
                    val url = it.get(22)
                    Log.d(Constants.TAG, "getVideoInfo failed")
                }
            }
        }
//        youtubeExtractor.extract("https://www.youtube.com/watch?v=AdtJggQORBI", true, true)
        youtubeExtractor.extract("https://www.youtube.com/watch?v=Qgvip8ISSF0"	, true, true)
    }
}