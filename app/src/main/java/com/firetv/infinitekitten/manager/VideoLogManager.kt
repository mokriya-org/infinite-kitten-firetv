package com.firetv.infinitekitten.manager

import android.content.Context
import com.firetv.infinitekitten.App

/**
 * Created by dileepan on 12/03/18.
 */
object VideoLogManager {

    private const val PREF_NAME = "prefInfiniteKitten"

    private val prefs = App.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun addVideoId(videoId: String, playlistId: String) {
        val logList = logListOf(playlistId)
        logList.add(videoId)

        prefs.edit().putString(keyForPlaylistId(playlistId), logList.joinToString(",")).apply()
    }

    fun isSeen(videoId: String, playlistId: String): Boolean = logListOf(playlistId).contains(videoId)

    fun clearLog(playlistId: String) = prefs.edit().putString(keyForPlaylistId(playlistId), "").apply()

    private fun logListOf(playlistId: String) = ArrayList<String>().apply {
        addAll(prefs.getString(keyForPlaylistId(playlistId), "").split(","))
    }

    private fun keyForPlaylistId(playlistId: String) = "watched_${playlistId}_videos"
}