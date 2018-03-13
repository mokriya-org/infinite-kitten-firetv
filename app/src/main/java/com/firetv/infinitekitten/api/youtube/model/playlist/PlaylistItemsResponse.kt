package com.firetv.infinitekitten.api.youtube.model.playlist

/**
 * Created by diogobrito on 08/03/2018.
 */
data class PlaylistItemsResponse(
        val items: List<PlaylistItem>,
        val pageToken: String,
        val nextPageToken: String,
        val prevPageToken: String
)