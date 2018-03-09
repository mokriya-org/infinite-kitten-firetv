package com.firetv.infinitekitten.api.model.video

import java.net.URL

/**
 * Created by diogobrito on 08/03/2018.
 */
data class Thumbnail(
        val url: URL,
        val width: Int,
        val height: Int
)