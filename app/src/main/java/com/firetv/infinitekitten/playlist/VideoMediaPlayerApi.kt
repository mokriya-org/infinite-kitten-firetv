package com.firetv.infinitekitten.playlist

import android.net.Uri
import android.support.annotation.FloatRange
import android.support.annotation.IntRange
import com.devbrackets.android.exomedia.listener.*
import com.devbrackets.android.exomedia.ui.widget.VideoView
import com.devbrackets.android.playlistcore.api.MediaPlayerApi
import com.devbrackets.android.playlistcore.data.PlaybackState
import com.devbrackets.android.playlistcore.listener.MediaStatusListener
import com.devbrackets.android.playlistcore.listener.PlaylistListener
import com.firetv.infinitekitten.model.VideoPlaylistItem

/**
 * Created by diogobrito on 09/03/2018.
 */
class VideoMediaPlayerApi(var videoView: VideoView) :
        MediaPlayerApi<VideoPlaylistItem>,
        PlaylistListener<VideoPlaylistItem>,
        OnPreparedListener,
        OnCompletionListener,
        OnErrorListener,
        OnSeekCompletionListener,
        OnBufferUpdateListener {

    private var prepared: Boolean = false
    private var bufferPercent: Int = 0
    private var mediaStatusListener: MediaStatusListener<VideoPlaylistItem>? = null

    override val isPlaying: Boolean
        get() = videoView.isPlaying
    override val bufferedPercent: Int
        get() = bufferPercent
    override val currentPosition: Long
        get() = if (prepared) videoView.currentPosition else 0
    override val duration: Long
        get() = if (prepared) videoView.duration else 0
    override val handlesOwnAudioFocus: Boolean
        get() = false

    init {
        videoView.setOnErrorListener(this)
        videoView.setOnPreparedListener(this)
        videoView.setOnCompletionListener(this)
        videoView.setOnSeekCompletionListener(this)
        videoView.setOnBufferUpdateListener(this)
    }

    override fun play() {
        videoView.start()
    }

    override fun pause() {
        videoView.pause()
    }

    override fun stop() {
        videoView.stopPlayback()
    }

    override fun reset() {
        // Purposefully left blank
    }

    override fun release() {
        videoView.suspend()
    }

    override fun setVolume(@FloatRange(from = 0.0, to = 1.0) left: Float, @FloatRange(from = 0.0, to = 1.0) right: Float) {
        videoView.setVolume((left + right) / 2)
    }

    override fun seekTo(@IntRange(from = 0L) milliseconds: Long) {
        videoView.seekTo(milliseconds.toInt().toLong())
    }

    override fun handlesItem(item: VideoPlaylistItem) = true

    override fun playItem(item: VideoPlaylistItem) {
        prepared = false
        bufferPercent = 0
        videoView.setVideoURI(Uri.parse(item.mediaUrl))
    }

    /*
     * PlaylistListener methods used for keeping the VideoControls provided
     * by the ExoMedia VideoView up-to-date with the current playback state
     */
    override fun onPlaylistItemChanged(currentItem: VideoPlaylistItem?, hasNext: Boolean, hasPrevious: Boolean): Boolean {
        val videoControls = videoView.videoControls

        if (videoControls != null && currentItem != null) {
            // Updates the VideoControls display text
            videoControls.setTitle(currentItem!!.title)
            videoControls.setDescription(currentItem!!.youtubeDescription)

            // Updates the VideoControls button visibilities
            videoControls.setPreviousButtonEnabled(hasPrevious)
            videoControls.setNextButtonEnabled(hasNext)
        }

        return false
    }

    override fun onPlaybackStateChanged(playbackState: PlaybackState): Boolean {
        return false
    }

    override fun setMediaStatusListener(listener: MediaStatusListener<VideoPlaylistItem>) {
        mediaStatusListener = listener
    }

    override fun onCompletion() {
        mediaStatusListener?.onCompletion(this)
    }

    override fun onError(e: Exception): Boolean {
        val mediaStatusListener = this.mediaStatusListener
        return mediaStatusListener != null && mediaStatusListener.onError(this)
    }

    override fun onPrepared() {
        prepared = true
        mediaStatusListener?.onPrepared(this)
    }

    override fun onSeekComplete() {
        mediaStatusListener?.onSeekComplete(this)
    }

    override fun onBufferingUpdate(percent: Int) {
        bufferPercent = percent
        mediaStatusListener?.onBufferingUpdate(this, percent)
    }

}
