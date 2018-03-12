package com.firetv.infinitekitten.manager

import android.content.Context
import com.firetv.infinitekitten.App
import com.firetv.infinitekitten.api.ApiConstants

/**
 * Created by dileepan on 12/03/18.
 */
object VideoLogManager {

    private const val PREF_NAME = "prefInfiniteKitten"
    private const val KEY_VIDEO_HISTORY_CATS = "keyVideoHistoryCats"
    private const val KEY_VIDEO_HISTORY_HUMANS = "keyVideoHistoryHumans"

    private val prefs = App.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val catVideoLogList = ArrayList<String>().apply {
        addAll(prefs.getString(KEY_VIDEO_HISTORY_CATS, "").split(","))
    }

    private val humanVideoLogList = ArrayList<String>().apply {
        addAll(prefs.getString(KEY_VIDEO_HISTORY_HUMANS, "").split(","))
    }

    fun addVideoId(videoId: String, playlistId: String) = when (playlistId) {
        ApiConstants.CAT_PLAYLIST -> {
            catVideoLogList.add(videoId)
            prefs.edit().putString(KEY_VIDEO_HISTORY_CATS, catVideoLogList.joinToString(",")).apply()
        }
        else -> {
            humanVideoLogList.add(videoId)
            prefs.edit().putString(KEY_VIDEO_HISTORY_HUMANS, humanVideoLogList.joinToString(",")).apply()
        }
    }

    fun isSeen(videoId: String, playlistId: String): Boolean = when (playlistId) {
        ApiConstants.CAT_PLAYLIST -> catVideoLogList.contains(videoId)
        else -> humanVideoLogList.contains(videoId)
    }

    fun clearLog(playlistId: String) = prefs.edit().putString(when (playlistId) {
        ApiConstants.CAT_PLAYLIST -> KEY_VIDEO_HISTORY_CATS
        else -> KEY_VIDEO_HISTORY_HUMANS
    }, "").apply()

}