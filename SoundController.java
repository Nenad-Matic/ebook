package com.example.nenad.eudzbenik;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;


public class SoundController {

    private final float VOLUME_MAX = 1.0f;
    private final float VOLUME_HALF = 0.15f;
    private final float VOLUME_MUTED = 0.0f;


    private Context appContext;

    private SoundPool fxPool;
    private SoundPool speechPool;

    private MediaPlayer speechPlayer;
    private MediaPlayer musicPlayer;

    private AudioAttributes attribsBackMusic;
    private AudioAttributes attribsSpeech;
    private AudioAttributes attribsSoundFX;

    public SoundController(Context c) {
        appContext = c;

        prepareAttributes();
        prepareSoundPools();
    }


    private int fxNowPlaying = 0;
    private int fxStream = 0;
    private float fxCurrentVolume = 1.0f;
    private boolean fxMuted = false;

    public void playFX(int rawID) {
        if (fxMuted)
            return;
        fxPool.stop(fxNowPlaying);
        fxPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                fxStream = fxPool.play(fxNowPlaying, 0.1f, 0.1f, 2, 0, 1);
                fxCurrentVolume = 1.0f;
            }
        });
        fxNowPlaying = fxPool.load(appContext, rawID, 2);
    }

    public void muteFX() {
        fxMuted = true;
    }
    public void unmuteFX() {
        fxMuted = false;
    }


    private boolean speechPlaying = false;
    private boolean speechMuted = false;

    public void playSpeech (int rawID) {
        if (speechPlayer != null)
            speechPlayer.release();

        speechPlayer = MediaPlayer.create(appContext, rawID);

        speechPlayer.setAudioAttributes(attribsSpeech);

        if (!speechMuted)
            speechPlayer.setVolume(VOLUME_MAX, VOLUME_MAX);
        else
            speechPlayer.setVolume(VOLUME_MUTED, VOLUME_MUTED);

        speechPlayer.start();

        speechPlaying = true;

        speechPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                speechPlayer.release();
                speechPlaying = false;
            }
        });
    }

    public boolean isSpeechMuted () {
        return speechMuted;
    }

    public void muteSpeech() {
        if (speechPlaying)
            speechPlayer.setVolume(VOLUME_MUTED, VOLUME_MUTED);
        speechMuted = true;
    }

    public void unmuteSpeech() {
        if (speechPlaying)
            speechPlayer.setVolume(VOLUME_MAX, VOLUME_MAX);
        speechMuted = false;
    }

    public void stopSpeech() {
        if (speechPlayer != null)
            speechPlayer.release();
        speechPlaying = false;
    }



    public void playBackMusic (int rawID) {
        if (rawID == 0) {
            musicPlayer.release();
            return;
        }
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            musicPlayer = MediaPlayer.create(appContext, rawID, attribsBackMusic, 1);
        else
            musicPlayer = MediaPlayer.create(appContext, rawID);

        musicPlayer.setLooping(true);
        musicPlayer.setVolume(VOLUME_HALF, VOLUME_HALF);

        musicPlayer.start();
    }


    public void muteMusic() {
        if (musicPlayer != null)
            musicPlayer.setVolume(VOLUME_MUTED, VOLUME_MUTED);
    }
    public void unmuteMusic() {
        if (musicPlayer != null)
            musicPlayer.setVolume(VOLUME_HALF, VOLUME_HALF);
    }

    private boolean isPaused = false;

    public void pauseMusic() {
        if (musicPlayer!= null)
            if (musicPlayer.isPlaying()) {
                musicPlayer.pause();
                isPaused = true;
            }
    }
    public void resumeMusic() {
        if (isPaused && musicPlayer!= null)
            musicPlayer.start();
        isPaused = false;
    }




    private void prepareSoundPools() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            fxPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(attribsSoundFX)
                    .build();

            speechPool = new SoundPool.Builder()
                    .setAudioAttributes(attribsSpeech)
                    .setMaxStreams(2)
                    .build();
        }
        else {
            fxPool = new SoundPool(5, AudioManager.STREAM_NOTIFICATION, 10);
            speechPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 10);
        }
    }

    private void prepareAttributes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attribsBackMusic = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            attribsSoundFX = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            attribsSpeech = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
        }
    }



    public void releaseController() {
        if (musicPlayer != null)
            musicPlayer.release();
        fxPool.release();
        speechPool.release();
        if (speechPlayer != null)
            speechPlayer.release();
    }
}
