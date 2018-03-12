package com.firetv.infinitekitten.api.youtube

import com.firetv.infinitekitten.api.ApiConstants
import com.firetv.infinitekitten.api.youtube.model.playlist.PlaylistItemsResponse
import com.firetv.infinitekitten.api.youtube.model.search.SearchResponse
import com.firetv.infinitekitten.api.youtube.model.video.VideosResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface YouTubeApiService {

    @GET("playlistItems")
    fun getPageForPlaylist(@Query("key") key: String = ApiConstants.YOUTUBE_API_KEY,
                           @Query("playlistId") playlistId: String,
                           @Query("part") part: String = "contentDetails",
                           @Query("maxResults") maxResults: Int = ApiConstants.YOUTUBE_RESULTS_PER_PAGE,
                           @Query("pageToken") pageToken: String? = null): Call<PlaylistItemsResponse>

    @GET("search")
    fun getPageForSearch(@Query("key") key: String = ApiConstants.YOUTUBE_API_KEY,
                         @Query("q") query: String,
                         @Query("topicId") topicId: String,
                         @Query("part") part: String = "snippet",
                         @Query("maxResults") maxResults: Int = ApiConstants.YOUTUBE_RESULTS_PER_PAGE,
                         @Query("type") type: String = "video",
                         @Query("videoEmbeddable") videoEmbeddable: Boolean = true,
                         @Query("pageToken") pageToken: String? = null): Call<SearchResponse>


    @GET("videos")
    fun getVideoInfo(@Query("key") key: String = ApiConstants.YOUTUBE_API_KEY,
                     @Query("id") videoId: String,
                     @Query("part") part: String = "snippet"): Call<VideosResponse>

    /**
     * Companion object to create the YouTubeApiService
     */
    companion object {
        fun create(): YouTubeApiService {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ApiConstants.YOUTUBE_API_URL)
                    .build()

            return retrofit.create(YouTubeApiService::class.java)
        }
    }
}