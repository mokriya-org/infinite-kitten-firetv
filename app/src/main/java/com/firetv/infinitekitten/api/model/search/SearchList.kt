package com.firetv.infinitekitten.api.model.search

/**
 * Created by diogobrito on 08/03/2018.
 */
data class SearchList(
        val items: List<SearchItem>,
        val pageToken: String,
        val nextPageToken: String,
        val prevPageToken: String
)