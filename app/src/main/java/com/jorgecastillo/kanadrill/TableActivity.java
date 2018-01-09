package com.jorgecastillo.kanadrill;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public abstract class TableActivity extends Activity {

    protected TextView textViewTabla;
    protected Resources myResources;
    protected String[] meaning;
    protected String[] japanese;
    protected SharedPreferences myPreferences;
    protected int theme_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        theme_list= Integer.parseInt(myPreferences.getString("theme_list", "1"));
        CommonCode.theme_list = theme_list;
        switch (theme_list){
            case 1:
                setTheme(R.style.HoloLight);
                break;
            case 2:
                setTheme(R.style.HoloDark);
                break;
            case 3:
                setTheme(R.style.MaterialLight);
                break;
            case 4:
                setTheme(R.style.MaterialDark);
                break;
            default:
                break;
        }

        setContentView(R.layout.activity_table);
        textViewTabla = (TextView) findViewById(R.id.textViewTabla);

        myResources = getResources();
        setArrays();
        fillTable();

    }

    abstract public void setArrays();

    public void fillTable() {
        int n = meaning.length;
        String textTable = "";
        for(int i = 0; i < n ; i++){
            textTable += "" + meaning[i] + japanese[i] + "  ";
            if ( (i + 1) % 5 == 0) {
                textTable += "\n\n";
            }
        }
        textViewTabla.setText(textTable);
        textViewTabla.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
