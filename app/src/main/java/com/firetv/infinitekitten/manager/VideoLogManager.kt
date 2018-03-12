package com.firetv.infinitekitten.manager

import android.content.Context
import com.firetv.infinitekitten.App

/**
 * Created by dileepan on 12/03/18.
 */
object VideoLogManager {

    private const val PREF_NAME = "prefInfiniteKitten"
    private const val KEY_VIDEO_HISTORY = "keyVideoHistory"

    private val prefs = App.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    val videoLogList = ArrayList<String>().apply {
        addAll(prefs.getString(KEY_VIDEO_HISTORY, "").split(","))
    }

    fun addVideoId(videoId: String) {
        videoLogList.add(videoId)
        prefs.edit().putString(KEY_VIDEO_HISTORY, videoLogList.joinToString(",")).apply()
    }

    fun clearLog() = prefs.edit().clear().apply()
}