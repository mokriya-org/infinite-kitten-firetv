package com.firetv.infinitekitten.api.model.video

/**
 * Created by diogobrito on 08/03/2018.
 */
data class Snippet(
        val channelTitle: String,
        val thumbnails: Thumbnails,
        val localized: Localized
)