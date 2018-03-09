package com.firetv.infinitekitten.playlist

import android.content.Context
import android.net.Uri
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.firetv.infinitekitten.api.youtube.YouTubeApiService
import com.firetv.infinitekitten.api.youtube.model.playlist.PlaylistItemsResponse
import com.firetv.infinitekitten.api.youtube.model.video.VideosResponse
import com.firetv.infinitekitten.data.YouTubeMediaItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by diogobrito on 09/03/2018.
 */


class YouTubeMediaItemFetcher(
        private val context: Context,
        private val playlistId: String,
        private val pageToken: String? = null) {

    companion object {
        val youTubeApiService by lazy {
            YouTubeApiService.create()
        }
    }

    interface YouTubeMediaItemFetcherCallback {
        fun onFailure()

        fun onSuccess(result: List<YouTubeMediaItem>, nextPageToken: String)
    }

    interface YouTubeMediaItemCallback {
        fun onFailure()

        fun onSuccess(result: YouTubeMediaItem)
    }

    interface YouTubeVideoItemUrlCallback {
        fun onFailure()

        fun onSuccess(url: String)
    }


    fun fetch(callback: YouTubeMediaItemFetcherCallback) {
        fetchVideoMediaItemList(callback)
    }

    private fun fetchVideoMediaItemList(callback: YouTubeMediaItemFetcherCallback) {
        youTubeApiService
                .getPageForPlaylist(playlistId = playlistId, pageToken = pageToken)
                .enqueue(object : Callback<PlaylistItemsResponse> {
                    override fun onFailure(call: Call<PlaylistItemsResponse>?, t: Throwable?) {
                        callback.onFailure()
                    }

                    override fun onResponse(call: Call<PlaylistItemsResponse>, response: Response<PlaylistItemsResponse>) {
                        if (!response.isSuccessful) {
                            callback.onFailure()
                            return
                        }

                        response.body()?.let { playlistItemsResponse ->
                            val videoIds = playlistItemsResponse.items.map { it.contentDetails.videoId }
                            val nextPageToken = playlistItemsResponse.nextPageToken

                            var returnedCallbacks = 0
                            var youtubeMediaItems = mutableListOf<YouTubeMediaItem>()

                            videoIds.forEach {
                                fetchVideoMediaItem(it, object : YouTubeMediaItemCallback {
                                    override fun onFailure() {
                                        if (++returnedCallbacks < videoIds.size) {
                                            return
                                        }

                                        if (youtubeMediaItems.isEmpty()) {
                                            callback.onFailure()
                                        } else {
                                            callback.onSuccess(youtubeMediaItems, nextPageToken)
                                        }
                                    }

                                    override fun onSuccess(youTubeMediaItem: YouTubeMediaItem) {
                                        youtubeMediaItems.add(youTubeMediaItem)

                                        if (++returnedCallbacks < videoIds.size) {
                                            return
                                        }
                                        callback.onSuccess(youtubeMediaItems, nextPageToken)
                                    }
                                })
                            }
                        }
                    }
                })
    }

    private fun fetchVideoMediaItem(videoId: String, callback: YouTubeMediaItemCallback) {
        youTubeApiService
                .getVideoInfo(videoId = videoId)
                .enqueue(object : Callback<VideosResponse> {
                    override fun onFailure(call: Call<VideosResponse>?, t: Throwable?) {
                        callback.onFailure()
                    }

                    override fun onResponse(call: Call<VideosResponse>, response: Response<VideosResponse>) {
                        if (!response.isSuccessful) {
                            callback.onFailure()
                            return
                        }

                        response.body()?.let {
                            val videoItem = it.items.first()

                            fetchVideoUrl(videoId, callback = object : YouTubeVideoItemUrlCallback {
                                override fun onFailure() {
                                    callback.onFailure()
                                }

                                override fun onSuccess(url: String) {
                                    val youtubeMediaItem = YouTubeMediaItem(
                                            youtubeId = videoId,
                                            youtubeTitle = videoItem.snippet.localized.title,
                                            youtubeDescription = videoItem.snippet.localized.description,
                                            youtubeMediaUrl = url,
                                            youtubeThumbnailUrl = videoItem.snippet.thumbnails.high.url,
                                            youtubeChannelTitle = videoItem.snippet.channelTitle)

                                    callback.onSuccess(youtubeMediaItem)
                                }
                            })
                        }

                    }
                })
    }

    private fun fetchVideoUrl(videoId: String, callback: YouTubeVideoItemUrlCallback) {
        val youtubeExtractor: YouTubeExtractor = object : YouTubeExtractor(context) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
                val videoItemUrl = ytFiles?.get(22)?.url
                if (videoItemUrl == null) {
                    callback.onFailure()
                } else {
                    callback.onSuccess(videoItemUrl)
                }
            }
        }

        youtubeExtractor.extract(
                buildYoutubeVideoUrl(videoId).toString(),
                true,
                true)
    }

    private fun buildYoutubeVideoUrl(videoId: String): Uri {
        return Uri.Builder()
                .scheme("https")
                .authority("www.youtube.com")
                .appendPath("watch")
                .appendQueryParameter("v", videoId)
                .build()
    }
}