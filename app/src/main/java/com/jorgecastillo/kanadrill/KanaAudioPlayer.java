package com.jorgecastillo.kanadrill;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class KanaAudioPlayer {
    private MediaPlayer mediaPlayer = null;
    private AudioManager audioManager = null;
    private AudioManager.OnAudioFocusChangeListener afChangeListener = afChangeListener = afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            }
        }
    };

    public KanaAudioPlayer(Activity activity) {
        audioManager = (AudioManager) activity.getSystemService(activity.AUDIO_SERVICE);
    }

    public void releaseMediaPlayer() {
        if(mediaPlayer!=null) {
            mediaPlayer.release();

            mediaPlayer = null;

            audioManager.abandonAudioFocus(afChangeListener);
        }
    }

    public void play(Context context,int id) {
        releaseMediaPlayer();

        mediaPlayer = MediaPlayer.create(context,id);
        audioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                releaseMediaPlayer();
            }
        });
    }
}
