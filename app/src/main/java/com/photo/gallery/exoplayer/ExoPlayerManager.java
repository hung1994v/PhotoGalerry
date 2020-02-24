package com.photo.gallery.exoplayer;

/**
 * Created by ad on 4/10/2019.
 */
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

import java.util.Collection;
import java.util.HashMap;

public final class ExoPlayerManager {

    private static ExoPlayerManager instance;

    public static ExoPlayerManager getInstance() {
        if (instance == null) {
            instance = new ExoPlayerManager();
        }
        return instance;
    }

    private HashMap<Integer, SimpleExoPlayer> playersInUse = new HashMap<>();

    private ExoPlayerManager() {
    }

    public void clearAllPlayers() {
        Collection<SimpleExoPlayer> players = playersInUse.values();
        for (SimpleExoPlayer player : players) {
            player.release();
        }
        playersInUse.clear();
    }

    public void stopPlayerFor(int playerKey) {
        if (playersInUse.containsKey(playerKey)) {
            playersInUse.get(playerKey).stop();
        }
    }

    public boolean setPlaybackParameters(int playerKey, float speed, float pitch) {
        if (playersInUse.containsKey(playerKey)) {
            SimpleExoPlayer simpleExoPlayer = playersInUse.get(playerKey);
            PlaybackParameters param = new PlaybackParameters(speed, pitch); // speed & pitch must be greater than 0
            simpleExoPlayer.setPlaybackParameters(param);
            return  true;
        }
        return false;
    }

    public SimpleExoPlayer prepareExoPlayer(
            int playerKey,
            @NonNull Context context,
            @NonNull PlayerView simpleExoPlayerView,
            int currentWindow,
            boolean playWhenReady,
            long playbackPosition, MediaSource... mediaSources) {

        SimpleExoPlayer simpleExoPlayer = playersInUse.get(playerKey);

        if (simpleExoPlayer == null) {
            simpleExoPlayer = setUpMediaPlayer(context);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
            simpleExoPlayer.seekTo(currentWindow, playbackPosition);
            MergingMediaSource mediaSource =
                    new MergingMediaSource(mediaSources);

            simpleExoPlayer.prepare(mediaSource, true, false);
            playersInUse.put(playerKey, simpleExoPlayer);
        } else {
            simpleExoPlayer.seekTo(currentWindow, playbackPosition);
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
        }

        return simpleExoPlayer;
    }

    private SimpleExoPlayer setUpMediaPlayer(Context context) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);

        return ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl);
    }

    public static MediaSource setUpMediaSource(Context context, Uri mediUri) {
        final String APPLICATION_BASE_USER_AGENT = "kAppRD";
        final String userAgent = Util.getUserAgent(context, APPLICATION_BASE_USER_AGENT);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent, defaultBandwidthMeter);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediUri);
        return mediaSource;
    }

    public static MediaSource setUpSubtitleSource(Context context, Uri uri) {
        final String APPLICATION_BASE_USER_AGENT = "kAppRD";
        final String userAgent = Util.getUserAgent(context, APPLICATION_BASE_USER_AGENT);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent, defaultBandwidthMeter);

        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                Format.NO_VALUE,"en");
        MediaSource source = new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(uri, textFormat, C.TIME_UNSET);

        return source;
    }

    public void releaseExoPlayer(int playerKey) {
        if (playersInUse.containsKey(playerKey)) {
            SimpleExoPlayer simpleExoPlayer = playersInUse.remove(playerKey);
            simpleExoPlayer.release();
        }
    }

}
