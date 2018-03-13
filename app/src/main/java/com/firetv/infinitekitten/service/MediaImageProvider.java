package com.firetv.infinitekitten.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.devbrackets.android.playlistcore.components.image.ImageProvider;
import com.firetv.infinitekitten.R;
import com.firetv.infinitekitten.model.VideoPlaylistItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MediaImageProvider implements ImageProvider<VideoPlaylistItem> {
    interface OnImageUpdatedListener {
        void onImageUpdated();
    }

    @NotNull
    private RequestManager glide;
    @NonNull
    private OnImageUpdatedListener listener;

    @NonNull
    private NotificationImageTarget notificationImageTarget = new NotificationImageTarget();
    @NonNull
    private RemoteViewImageTarget remoteViewImageTarget = new RemoteViewImageTarget();

    @NonNull
    private Bitmap defaultNotificationImage;

    @Nullable
    private Bitmap notificationImage;
    @Nullable
    private Bitmap artworkImage;

    public MediaImageProvider(@NonNull Context context, @NonNull OnImageUpdatedListener listener) {
        glide = Glide.with(context.getApplicationContext());
        this.listener = listener;
        defaultNotificationImage = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_status_bar);
    }

    @Override
    public int getNotificationIconRes() {
        return R.mipmap.ic_status_bar;
    }

    @Override
    public int getRemoteViewIconRes() {
        return R.mipmap.ic_status_bar;
    }

    @Nullable
    @Override
    public Bitmap getLargeNotificationImage() {
        return notificationImage != null ? notificationImage : defaultNotificationImage;
    }

    @Nullable
    @Override
    public Bitmap getRemoteViewArtwork() {
        return artworkImage;
    }

    @Override
    public void updateImages(@NotNull VideoPlaylistItem playlistItem) {
        glide.load(playlistItem.getThumbnailUrl()).asBitmap().into(notificationImageTarget);
        glide.load(playlistItem.getArtworkUrl()).asBitmap().into(remoteViewImageTarget);
    }

    /**
     * A class used to listen to the loading of the large notification images and perform
     * the correct functionality to update the notification once it is loaded.
     * <p>
     * <b>NOTE:</b> This is a Glide Image loader class
     */
    private class NotificationImageTarget extends SimpleTarget<Bitmap> {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            notificationImage = resource;
            listener.onImageUpdated();
        }
    }

    /**
     * A class used to listen to the loading of the large lock screen images and perform
     * the correct functionality to update the artwork once it is loaded.
     * <p>
     * <b>NOTE:</b> This is a Glide Image loader class
     */
    private class RemoteViewImageTarget extends SimpleTarget<Bitmap> {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            artworkImage = resource;
            listener.onImageUpdated();
        }
    }
}
