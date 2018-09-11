package com.jorgecastillo.kanadrill;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public abstract class DrillActivity extends EveryActivity implements DialogInterface.OnDismissListener{

    protected TextView gameText;
    protected Button button1, button2, button3, button4;

    protected int count;
    protected int upto;
    protected int incorrect;

    protected int[] order;
    private int[] buttonValues = new int[4];
    protected Resources myResources;

    protected String[] meaning;
    protected String[] japanese;

    protected Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameText = (TextView) findViewById(R.id.gameText);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        myResources = getResources();

        setArrays();

        if (myPreferences.getBoolean("setup_true", false)) {

            int kana_list = Integer.parseInt(myPreferences.getString("kana_list", "1"));

            upto = CommonCode.setUpto(kana_list);

            order = new int[upto];

            CommonCode.orderRandom(upto, order);

            setButtons();

        }
    }

    public abstract void setArrays();

    public void onClickButton1(View view) { everyButton(0); }

    public void onClickButton2(View view) { everyButton(1); }

    public void onClickButton3(View view) { everyButton(2); }

    public void onClickButton4(View view) { everyButton(3); }

    public void onClickGameText(View view) {
        wrongKana(order[count]);
        count++;
        setButtons();
    }

    public void everyButton(int value) {

        if (order[count] == buttonValues[value]) {
            count++;
            setButtons();
        } else {
            incorrect++;
            wrongKana(order[count]);
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        count++;
        setButtons();
    }

    public void wrongKana(int count) {
        KanaDrillDialog kdd = new KanaDrillDialog();
        kdd.setTitle(getString(R.string.wrong_kana));
        kdd.setValues(japanese[count], " = " + meaning[count]);
        kdd.show(getFragmentManager(), "Kana Dialog");
    }

    private void setButtons() {

        if (count >= upto) {
            Toast.makeText(getApplicationContext(), incorrect + " incorrect", Toast.LENGTH_LONG).show();
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

        if(random.nextBoolean()) {
            gameText.setText(japanese[order[count]]);
            button1.setText(meaning[buttonValues[0]]);
            button2.setText(meaning[buttonValues[1]]);
            button3.setText(meaning[buttonValues[2]]);
            button4.setText(meaning[buttonValues[3]]);
        }else{
            gameText.setText(meaning[order[count]]);
            button1.setText(japanese[buttonValues[0]]);
            button2.setText(japanese[buttonValues[1]]);
            button3.setText(japanese[buttonValues[2]]);
            button4.setText(japanese[buttonValues[3]]);
        }
    }
}
