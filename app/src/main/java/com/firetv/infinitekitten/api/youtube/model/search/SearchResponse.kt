package com.firetv.infinitekitten.api.youtube.model.search

/**
 * Created by diogobrito on 08/03/2018.
 */
data class SearchResponse(
        val items: List<SearchItem>,
        val pageToken: String,
        val nextPageToken: String,
        val prevPageToken: String
)