package com.jorgecastillo.kanadrill;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class HiraganaDrawActivity extends DrawActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void setArrays() {
        meaning = myResources.getStringArray(R.array.romanji);
        japanese = myResources.getStringArray(R.array.hiragana);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        Intent intent = new Intent(this, HiraganaDrawActivity.class);
        startActivity(intent);
    }
}

