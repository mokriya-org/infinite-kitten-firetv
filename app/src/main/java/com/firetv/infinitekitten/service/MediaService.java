package com.firetv.infinitekitten.service;


import android.support.annotation.NonNull;

import com.devbrackets.android.playlistcore.api.MediaPlayerApi;
import com.devbrackets.android.playlistcore.components.playlisthandler.DefaultPlaylistHandler;
import com.devbrackets.android.playlistcore.components.playlisthandler.PlaylistHandler;
import com.devbrackets.android.playlistcore.service.BasePlaylistService;
import com.firetv.infinitekitten.App;
import com.firetv.infinitekitten.data.YouTubeMediaItem;
import com.firetv.infinitekitten.manager.PlaylistManager;

/**
 * A simple service that extends {@link BasePlaylistService} in order to provide
 * the application specific information required.
 */
public class MediaService extends BasePlaylistService<YouTubeMediaItem, PlaylistManager> {

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Releases and clears all the MediaPlayersMediaImageProvider
        for (MediaPlayerApi<YouTubeMediaItem> player : getPlaylistManager().getMediaPlayers()) {
            player.release();
        }

        getPlaylistManager().getMediaPlayers().clear();
    }

    @NonNull
    @Override
    protected PlaylistManager getPlaylistManager() {
        return App.playlistManager;
    }

    @NonNull
    @Override
    public PlaylistHandler<YouTubeMediaItem> newPlaylistHandler() {
        MediaImageProvider imageProvider = new MediaImageProvider(getApplicationContext(), new MediaImageProvider.OnImageUpdatedListener() {
            @Override
            public void onImageUpdated() {
                getPlaylistHandler().updateMediaControls();
            }
        });

        return new DefaultPlaylistHandler.Builder<>(
                getApplicationContext(),
                getClass(),
                getPlaylistManager(),
                imageProvider
        ).build();
    }
}