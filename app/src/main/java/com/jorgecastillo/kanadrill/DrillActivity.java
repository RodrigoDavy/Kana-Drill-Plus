package com.jorgecastillo.kanadrill;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class DrillActivity extends EveryActivity implements DialogInterface.OnDismissListener{

    protected TextView gameText;
    protected Button button1, button2, button3, button4;

    protected int count;
    protected int incorrect;

    protected List<Integer> order;
    private final List<Integer> buttonValues = new ArrayList<Integer>();
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
            Collection<String> kanaGroups = myPreferences.getStringSet("kana_groups", Collections.<String>emptySet());

            order = CommonCode.getKanas(kanaGroups);
            order.remove((Integer) 52);  // じ and ぢ are both ji
            order.remove((Integer) 57);  // ず and づ are both zu

            Collections.shuffle(order);

            setButtons();

        }
    }

    public abstract void setArrays();

    public void onClickButton1(View view) { everyButton(0); }

    public void onClickButton2(View view) { everyButton(1); }

    public void onClickButton3(View view) { everyButton(2); }

    public void onClickButton4(View view) { everyButton(3); }

    public void everyButton(int value) {

        if (order.get(count).equals(buttonValues.get(value))) {
            count++;
            setButtons();
        } else {
            incorrect++;
            wrongKana(order.get(count));
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

        if (count >= order.size()) {
            Toast.makeText(getApplicationContext(), incorrect + " incorrect", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        buttonValues.clear();
        buttonValues.addAll(order);
        buttonValues.remove((Integer) order.get(count));
        Collections.shuffle(buttonValues);
        // ensure we have at least 4 values
        while (buttonValues.size() < 4) {
            buttonValues.add(order.get(random.nextInt(order.size())));
        }
        buttonValues.subList(3, buttonValues.size()).clear();
        buttonValues.add(order.get(count));
        Collections.shuffle(buttonValues);

        if(random.nextBoolean()) {
            gameText.setText(japanese[order.get(count)]);
            button1.setText(meaning[buttonValues.get(0)]);
            button2.setText(meaning[buttonValues.get(1)]);
            button3.setText(meaning[buttonValues.get(2)]);
            button4.setText(meaning[buttonValues.get(3)]);
        }else{
            gameText.setText(meaning[order.get(count)]);
            button1.setText(japanese[buttonValues.get(0)]);
            button2.setText(japanese[buttonValues.get(1)]);
            button3.setText(japanese[buttonValues.get(2)]);
            button4.setText(japanese[buttonValues.get(3)]);
        }
    }
}
