package com.firetv.infinitekitten.api.flickr

import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firetv.infinitekitten.App
import com.firetv.infinitekitten.api.flickr.model.PhotoSizesResponse
import com.firetv.infinitekitten.api.flickr.model.SearchResponse
import com.firetv.infinitekitten.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by dileepan on 09/03/18.
 */
object Flickr {

    private val urlList = ArrayList<String>()

    val flickrApiService by lazy { FlickrApiService.create() }

    fun initUrls() {
        flickrApiService.getPhotos(query = "kitten", page = 1).enqueue(object : Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>?, t: Throwable?) {
                Log.d(Constants.TAG, "getPhotos failed")
                // Url list is empty
            }

            override fun onResponse(call: Call<SearchResponse>?, response: Response<SearchResponse>?) {
                Log.d(Constants.TAG, "getPhotos success")
                val photoList = response?.body()?.photos?.photo ?: return

                photoList.forEach { photo ->
                    flickrApiService.getPhotosSizes(photoId = photo.id).enqueue(object : Callback<PhotoSizesResponse> {
                        override fun onResponse(call: Call<PhotoSizesResponse>?, response: Response<PhotoSizesResponse>?) {
                            val goodResolutionPhotoSize = response?.body()?.sizes?.size?.firstOrNull { isGoodResolution(it.width.toDouble(), it.height.toDouble()) }
                                    ?: return

                            urlList.add(goodResolutionPhotoSize.source)
                            Glide.with(App.context).load(goodResolutionPhotoSize.source).diskCacheStrategy(DiskCacheStrategy.ALL).preload()
                        }

                        override fun onFailure(call: Call<PhotoSizesResponse>?, t: Throwable?) {
                            // Photo doesn't get added to Url list
                        }
                    })
                }
            }
        })
    }

    private fun isGoodResolution(width: Double, height: Double) = width >= 1920 && height >= 1080 && width / height == 16.0 / 9.0

    fun getFlickPhotoUrl(): String? = if (urlList.size > 0) urlList[Random().nextInt(urlList.size)] else null
}