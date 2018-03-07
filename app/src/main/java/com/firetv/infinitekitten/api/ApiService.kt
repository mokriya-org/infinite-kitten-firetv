package com.firetv.infinitekitten.api

import com.firetv.infinitekitten.utils.APIUtil
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/activities")
    fun getActivities(@Query("key") code: String = APIUtil.API_KEY): Call<String>
}