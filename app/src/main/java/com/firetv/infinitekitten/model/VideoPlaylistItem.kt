package com.firetv.infinitekitten.model

import com.devbrackets.android.playlistcore.api.PlaylistItem


/**
 * Created by diogobrito on 09/03/2018.
 */
class VideoPlaylistItem(
        val youtubeId: String,
        val youtubeTitle: String?,
        val youtubeDescription: String?,
        val youtubeMediaUrl: String,
        val youtubeThumbnailUrl: String?,
        val youtubeChannelTitle: String?
) : PlaylistItem {

    override val album: String? = null
    override val artist: String? = youtubeChannelTitle
    override val artworkUrl: String? = null
    override val downloaded: Boolean = false
    override val downloadedMediaUri: String? = null
    override val id: Long = youtubeId.hashCode().toLong()
    override val mediaType = 0
    override val mediaUrl: String? = youtubeMediaUrl
    override val thumbnailUrl: String? = youtubeThumbnailUrl
    override val title: String? = youtubeTitle
}