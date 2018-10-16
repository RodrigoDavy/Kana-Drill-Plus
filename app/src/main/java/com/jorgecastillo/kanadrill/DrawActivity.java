package com.jorgecastillo.kanadrill;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class DrawActivity extends EveryActivity implements DialogInterface.OnDismissListener {

    protected TextView gameText;
    protected Button button1, button2, button3;
    protected SimpleDrawingView simpleDrawingView;

    protected int count;
    protected int upto;
    protected int revealedCount;
    protected boolean revealed;

    protected List<Integer> order;
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
            Collection<String> kanaGroups = myPreferences.getStringSet("kana_groups", Collections.<String>emptySet());

            order = CommonCode.getKanas(kanaGroups);

            Collections.shuffle(order);

            kanaAudioPlayer.play(this, sounds[order.get(count)]);

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
        if (meaning[order.get(count)].contentEquals(gameText.getText())) {
            gameText.setText(japanese[order.get(count)]);
            button2.setText(R.string.hide);
            revealed = true;
        } else {
            gameText.setText(meaning[order.get(count)]);
            button2.setText(R.string.reveal);
        }
    }

    //Goes to next kana
    public void onClickButton3(View view) {
        KanaDrillDialog kdd = new KanaDrillDialog();
        kdd.setTitle(getString(R.string.correct_kana));
        kdd.setValues(japanese[order.get(count)], " = " + meaning[order.get(count)]);
        kdd.show(getFragmentManager(), "Kana Dialog");

    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        count++;
        if (revealed) {
            ++revealedCount;
            revealed = false;
        }
        setButtons();
        simpleDrawingView.erase();
        button2.setText(R.string.reveal);
        if (count < order.size()) {
            kanaAudioPlayer.play(DrawActivity.this, sounds[order.get(count)]);
        }
    }

    public void onClickGameText(View view) {
        if(order.get(count) < sounds.length) {
            kanaAudioPlayer.play(this, sounds[order.get(count)]);
        }
    }

    private void setButtons() {

        if (count >= order.size()) {
            Toast.makeText(getApplicationContext(), revealedCount + " revealed", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        gameText.setText(meaning[order.get(count)]);

    }
}
