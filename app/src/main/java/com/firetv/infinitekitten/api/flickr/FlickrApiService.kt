package com.firetv.infinitekitten.api.flickr

import com.firetv.infinitekitten.api.ApiConstants
import com.firetv.infinitekitten.api.flickr.model.PhotoSizesResponse
import com.firetv.infinitekitten.api.flickr.model.SearchResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


interface FlickrApiService {

    @GET("?method=flickr.photos.search")
    fun getPhotos(@Query("api_key") key: String = ApiConstants.FLICKR_API_KEY,
                  @Query("user_id") userId: String = "110705684@N04",
                  @Query("safe_search") safeSearch: Int = 1,
                  @Query("media") media: String = "photos",
                  @Query("content_type") contentType: Int = 1,
                  @Query("text") query: String,
                  @Query("tags") tags: String = "cat,cats,kitten,kittens",
                  @Query("per_page") hitsPerPage: Int = ApiConstants.FLICKR_RESULTS_PER_PAGE,
                  @Query("page") page: Int,
                  @Query("is_public") isPublic: Int = 1,
                  @Query("format") format: String = "json",
                  @Query("nojsoncallback") noJsonCallback: Int = 1,
                  @Query("group_id") groupId: String = "2918390@N25"): Call<SearchResponse>

    @GET("?method=flickr.photos.getSizes")
    fun getPhotosSizes(@Query("api_key") key: String = ApiConstants.FLICKR_API_KEY,
                       @Query("photo_id") photoId: String,
                       @Query("format") format: String = "json",
                       @Query("nojsoncallback") noJsonCallback: Int = 1): Call<PhotoSizesResponse>

    /**
     * Companion object to create the FlickrApiService
     */
    companion object {

        fun create(): FlickrApiService {
            val hostnameVerifier = HostnameVerifier(object : (String, SSLSession) -> Boolean {
                override fun invoke(hostname: String, session: SSLSession): Boolean {
                    return hostname == ApiConstants.FLICKR_API_DOMAIN
                }
            })

            val httpClient = OkHttpClient.Builder()
                    .hostnameVerifier(hostnameVerifier)
                    .build()

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ApiConstants.FLICKR_API_URL)
                    .client(httpClient)
                    .build()

            return retrofit.create(FlickrApiService::class.java)
        }
    }
}