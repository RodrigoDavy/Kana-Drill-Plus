package com.jorgecastillo.kanadrill;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class VocabularyTableActivity extends Activity {

    protected WebView kanaTable;
    protected Resources myResources;
    private final List<String> japanese = new ArrayList<>();
    private final List<String> meaning = new ArrayList<>();
    protected SharedPreferences myPreferences;
    protected int theme_list;
    private boolean darkTheme = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        theme_list = Integer.parseInt(myPreferences.getString("theme_list", "1"));
        CommonCode.theme_list = theme_list;
        switch (theme_list){
            case 1:
                setTheme(R.style.HoloLight);
                darkTheme = false;
                break;
            case 2:
                setTheme(R.style.HoloDark);
                darkTheme = true;
                break;
            case 3:
                setTheme(R.style.MaterialLight);
                darkTheme = false;
                break;
            case 4:
                setTheme(R.style.MaterialDark);
                darkTheme = true;
                break;
            default:
                break;
        }

        setContentView(R.layout.activity_table);
        kanaTable = (WebView) findViewById(R.id.kanaTable);
        kanaTable.setBackgroundColor(Color.TRANSPARENT);

        myResources = getResources();

        Collection<String> vocabGroups = myPreferences.getStringSet("vocab_groups", Collections.<String>emptySet());
        for (String fileName : vocabGroups) {
            try {
                CommonCode.addVocabularyFile(myResources, fileName, japanese, meaning);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        fillTable();
    }

    public void fillTable() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<style type=\"text/css\">");
        if (darkTheme) {
            builder.append("body{color: white}");
        }
        builder.append("</style>");
        builder.append("</head>");
        builder.append("<body>");
        builder.append("<table>");
        int n = meaning.size();
        for(int i = 0; i < n ; i++){
            builder.append("<tr>")
                   .append("<td>")
                   .append(japanese.get(i))
                   .append("</td><td>")
                   .append(meaning.get(i))
                   .append("</td>")
                   .append("</tr>");
        }
        builder.append("</table>");
        builder.append("</body>");
        builder.append("</html>");
        kanaTable.loadDataWithBaseURL(null, builder.toString(), "text/html", "utf-8", null);
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
