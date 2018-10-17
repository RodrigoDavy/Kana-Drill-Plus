package com.jorgecastillo.kanadrill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends EveryActivity {

    private SharedPreferences myPreferences;
    private Context myContext;
    private String textToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myContext = getApplicationContext();
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!myPreferences.getBoolean("setup_true", false)) {

            SharedPreferences.Editor editMyPreferences = myPreferences.edit();
            editMyPreferences.putBoolean("setup_true", true);
            editMyPreferences.commit();

            textToast = "Set Preferences";
            Toast.makeText(myContext, textToast, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }
    }

    public void onClickButtonHiraganaDrill(View view) {

        Intent intent = new Intent(this, HiraganaDrillActivity.class);
        startActivity(intent);
    }

    public void onClickButtonVocabulary(View view) {

        Intent intent = new Intent(this, VocabularyActivity.class);
        startActivity(intent);
    }

    public void onClickButtonKatakanaDrill(View view) {

        Intent intent = new Intent(this, KatakanaDrillActivity.class);
        startActivity(intent);
    }

    public void onClickButtonHiraganaTraining(View view) {

        Intent intent = new Intent(this, HiraganaTrainingActivity.class);
        startActivity(intent);
    }

    public void onClickButtonKatakanaTraining(View view) {

        Intent intent = new Intent(this, KatakanaTrainingActivity.class);
        startActivity(intent);
    }

    public void onClickButtonHiraganaTable(View view) {
        Intent intent = new Intent(this, HiraganaTableActivity.class);
        startActivity(intent);
    }

    public void onClickButtonKatakanaTable(View view) {
        Intent intent = new Intent(this, KatakanaTableActivity.class);
        startActivity(intent);
    }

    public void onClickButtonHiraganaDraw(View view) {

        Intent intent = new Intent(this, HiraganaDrawActivity.class);
        startActivity(intent);
    }

    public void onClickButtonKatakanaDraw(View view) {

        Intent intent = new Intent(this, KatakanaDrawActivity.class);
        startActivity(intent);
    }

    public void onClickButtonKanjiTraining(View view) {
        Intent intent = new Intent(this, KanjiTrainingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();

        //This is to change theme
        finish();
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.jorgecastillo.kanadrill");
        startActivity(launchIntent);
    }

}
