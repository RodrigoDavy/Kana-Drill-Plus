package com.jorgecastillo.kanadrill;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;

public class DrawActivity extends EveryActivity {

    protected TextView gameText;
    protected Button button1, button2, button3;
    protected SimpleDrawingView simpleDrawingView;

    protected int count;
    protected int upto;

    protected int[] order;
    private int[] buttonValues = new int[4];
    protected Resources myResources;

    protected String[] meaning;
    protected String[] japanese;
    protected int[] sounds;
    protected long startTime, tookyou;

    private MediaPlayer mediaPlayer = null;
    private AudioManager audioManager = null;
    private AudioManager.OnAudioFocusChangeListener afChangeListener = afChangeListener = afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);

        gameText = (TextView) findViewById(R.id.gameText);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        simpleDrawingView = (SimpleDrawingView) findViewById(R.id.drawing_view);

        myResources = getResources();

        setArrays();

        if (myPreferences.getBoolean("setup_true", false)) {

            int kana_list = Integer.parseInt(myPreferences.getString("kana_list", "1"));

            upto = CommonCode.setUpto(kana_list);

            order = new int[upto];

            CommonCode.orderRandom(upto, order);

            setButtons();

        }
        startTime = System.currentTimeMillis();
    }

    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if(mediaPlayer!=null) {
            mediaPlayer.release();

            mediaPlayer = null;

            audioManager.abandonAudioFocus(afChangeListener);
        }
    }

    public void setArrays() {}

    //Clears the canvas
    public void onClickButton1(View view) {
        simpleDrawingView.erase();
    }

    //Switch between romaji and kana
    public void onClickButton2(View view) {
        if (gameText.getText().equals(meaning[order[count]])) {
            gameText.setText(japanese[order[count]]);
            button2.setText(R.string.romaji);
        } else {
            gameText.setText(meaning[order[count]]);
            button2.setText(R.string.kana);
        }
    }

    //Goes to next kana
    public void onClickButton3(View view) {
        count++;
        setButtons();
        simpleDrawingView.erase();
        button2.setText(R.string.kana);
    }

    public void onClickGameText(View view) {
        if(order[count]<sounds.length) {
            releaseMediaPlayer();

            mediaPlayer = MediaPlayer.create(this, sounds[order[count]]);
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

    private void setButtons() {

        if (count >= upto) {
            tookyou = System.currentTimeMillis() - startTime;
            String filename = "took_you";
            String took_you = "" + (tookyou / 1000);
            FileOutputStream outputStream;
            Context mContext = getApplicationContext();
            try {
                outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(took_you.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

        int[] used_values = new int[4];
        int filled = CommonCode.randomInt(4, -1);
        used_values[0] = order[count];
        buttonValues[filled] = used_values[0];
        int uvct = 1;
        int skip = -1;
        switch(order[count]){
            case 52:
                skip = 57;
                break;
            case 57:
                skip = 52;
                break;
            default:
                break;
        }

        for (int i = 0; i < 4; i++) {
            if (i == filled) continue;
            int val = CommonCode.randomInt(upto, skip);
            for (int j = 0; j < uvct; j++) {
                while (val == used_values[j]) {
                    val = CommonCode.randomInt(upto, skip);
                    j = 0;
                }
            }
            used_values[uvct++] = val;
            buttonValues[i] = val;
        }

        gameText.setText(meaning[order[count]]);

    }
}