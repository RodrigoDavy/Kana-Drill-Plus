package com.jorgecastillo.kanadrill;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileOutputStream;

public abstract class DrawActivity extends EveryActivity implements DialogInterface.OnDismissListener {

    protected TextView gameText;
    protected Button button1, button2, button3;
    protected SimpleDrawingView simpleDrawingView;

    protected int count;
    protected int upto;
    protected int incorrect;

    protected int[] order;
    private int[] buttonValues = new int[4];
    protected Resources myResources;

    protected String[] meaning;
    protected String[] japanese;
    protected int[] sounds;

    protected KanaAudioPlayer kanaAudioPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        kanaAudioPlayer = new KanaAudioPlayer(this);

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

            kanaAudioPlayer.play(this,sounds[order[count]]);

            setButtons();

        }

        //need to do this otherwise the volume icon gets invisible depending on the theme
        switch(myPreferences.getString("theme_list","0")) {
            case "2":
            case "4":
                ImageView img = (ImageView) findViewById(R.id.volume_icon);
                img.setImageResource(R.drawable.ic_volume_up_white_24dp);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        kanaAudioPlayer.releaseMediaPlayer();
    }

    abstract public void setArrays();

    //Clears the canvas
    public void onClickButton1(View view) {
        simpleDrawingView.erase();
    }

    //Switch between romaji and kana
    public void onClickButton2(View view) {
        if (gameText.getText().equals(meaning[order[count]])) {
            gameText.setText(japanese[order[count]]);
            button2.setText(R.string.hide);
        } else {
            gameText.setText(meaning[order[count]]);
            button2.setText(R.string.reveal);
        }
    }

    //Goes to next kana
    public void onClickButton3(View view) {
        KanaDrillDialog kdd = new KanaDrillDialog();
        kdd.setTitle(getString(R.string.correct_kana));
        kdd.setValues(japanese[order[count]], " = " + meaning[order[count]]);
        kdd.show(getFragmentManager(), "Kana Dialog");

    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        count++;
        setButtons();
        simpleDrawingView.erase();
        button2.setText(R.string.reveal);
        kanaAudioPlayer.play(DrawActivity.this,sounds[order[count]]);
    }

    public void onClickGameText(View view) {
        if(order[count]<sounds.length) {
            kanaAudioPlayer.play(this,sounds[order[count]]);
        }
    }

    private void setButtons() {

        if (count >= upto) {
            MainActivity.incorrect = incorrect;
            finish();
            return;
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