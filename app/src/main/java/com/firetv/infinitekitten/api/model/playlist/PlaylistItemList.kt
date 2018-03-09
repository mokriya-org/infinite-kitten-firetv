package com.firetv.infinitekitten.api.model.playlist

/**
 * Created by diogobrito on 08/03/2018.
 */
data class PlaylistItemList(
        val items: List<PlaylistItem>,
        val pageToken: String,
        val nextPageToken: String,
        val prevPageToken: String
)