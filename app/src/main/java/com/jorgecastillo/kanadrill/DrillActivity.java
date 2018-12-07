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
    protected final List<Button> buttons = new ArrayList<Button>();

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
        buttons.add((Button) findViewById(R.id.button1));
        buttons.add((Button) findViewById(R.id.button2));
        buttons.add((Button) findViewById(R.id.button3));
        buttons.add((Button) findViewById(R.id.button4));

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

    public void onClickButton(View view) {
        int index = Integer.parseInt(view.getTag().toString());
        everyButton(index);
    }

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
            Toast.makeText(getApplicationContext(), incorrect + " out of " + order.size() + " incorrect", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        buttonValues.clear();
        buttonValues.addAll(order);
        buttonValues.remove((Integer) order.get(count));
        Collections.shuffle(buttonValues);
        while (buttonValues.size() < buttons.size() - 1) {
            buttonValues.add(order.get(random.nextInt(order.size())));
        }
        buttonValues.subList(buttons.size() - 1, buttonValues.size()).clear();
        buttonValues.add(order.get(count));
        Collections.shuffle(buttonValues);

        if(random.nextBoolean()) {
            gameText.setText(japanese[order.get(count)]);
            for (int i = 0; i < buttons.size(); ++i) {
                buttons.get(i).setText(meaning[buttonValues.get(i)]);
            }
        }else{
            gameText.setText(meaning[order.get(count)]);
            for (int i = 0; i < buttons.size(); ++i) {
                buttons.get(i).setText(japanese[buttonValues.get(i)]);
            }
        }
    }
}
