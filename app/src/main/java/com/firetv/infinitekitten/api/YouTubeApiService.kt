package com.firetv.infinitekitten.api

import com.firetv.infinitekitten.api.model.playlist.PlaylistItemList
import com.firetv.infinitekitten.api.model.search.SearchList
import com.firetv.infinitekitten.api.model.video.VideoList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface YouTubeApiService {

    @GET("playlistItems")
    fun getPageForPlaylist(@Query("key") key: String = ApiConstants.API_KEY,
                           @Query("playlistId") playlistId: String,
                           @Query("part") part: String = "contentDetails",
                           @Query("maxResults") maxResults: Int = 10,
                           @Query("pageToken") pageToken: String? = null): Call<PlaylistItemList>

    @GET("search")
    fun getPageForSearch(@Query("key") key: String = ApiConstants.API_KEY,
                         @Query("q") query: String,
                         @Query("topicId") topicId: String,
                         @Query("part") part: String = "snippet",
                         @Query("maxResults") maxResults: Int = 10,
                         @Query("type") type: String = "video",
                         @Query("videoEmbeddable") videoEmbeddable: Boolean = true,
                         @Query("pageToken") pageToken: String? = null): Call<SearchList>


    @GET("videos")
    fun getVideoInfo(@Query("key") key: String = ApiConstants.API_KEY,
                     @Query("id") videoId: String,
                     @Query("part") part: String = "snippet"): Call<VideoList>

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