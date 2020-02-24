package com.photo.gallery.exoplayer;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class PlayerEventListener implements Player.EventListener {

    private final SimpleExoPlayer exoPlayer;

    public PlayerEventListener(SimpleExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    public void onPlayerError(ExoPlaybackException error) {

    }

    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    public void onRepeatModeChanged(@Player.RepeatMode int repeatMode) {

    }

    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}