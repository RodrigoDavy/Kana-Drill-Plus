package com.jorgecastillo.kanadrill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class KatakanaDrawActivity extends DrawActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void setArrays() {
        meaning = myResources.getStringArray(R.array.romanji);
        japanese = myResources.getStringArray(R.array.katakana);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        Intent intent = new Intent(this, KatakanaDrawActivity.class);
        startActivity(intent);
    }

}