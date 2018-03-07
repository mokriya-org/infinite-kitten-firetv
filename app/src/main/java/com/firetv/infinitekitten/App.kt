package com.firetv.infinitekitten

import android.app.Application
import android.content.Context
import com.firetv.infinitekitten.utils.APIUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    companion object {
        lateinit var instance: App
        val context: Context get() = instance.applicationContext
        var retrofit: Retrofit? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        retrofit = Retrofit.Builder()
                .baseUrl(APIUtil.YOUTUBE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}