package com.superclassgroup.navigationdriving.utils;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by huyjackson on 7/14/17.
 */

public class StreamAudioIntentServiceUtils extends IntentService {

    public static final String ACTION_CHECK_AUDIO_COMPLETED = "ACTION_AUDIO_COMPLETED";

    MediaPlayer player;
    Intent broadCastIntent;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StreamAudioIntentServiceUtils() {
        super("StreamAudioIntentServiceUtils");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        broadCastIntent = new Intent();
        broadCastIntent.setAction(StreamAudioIntentServiceUtils.ACTION_CHECK_AUDIO_COMPLETED);


        String url = intent.getStringExtra("URL");
        if (url != null && !url.isEmpty()) {
            if (player == null) {
                player = new MediaPlayer();
            }
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                player.setDataSource(url);
                player.prepareAsync();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    broadCastIntent.putExtra("COMPLETED", false);
                    sendBroadcast(broadCastIntent);
                }
            });
        }

    }
}
