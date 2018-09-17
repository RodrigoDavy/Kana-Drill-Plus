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
import android.webkit.WebView;

public abstract class TableActivity extends Activity {

    protected WebView kanaTable;
    protected Resources myResources;
    protected String[] meaning;
    protected String[] japanese;
    protected SharedPreferences myPreferences;
    protected int theme_list;
    private boolean darkTheme = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        theme_list= Integer.parseInt(myPreferences.getString("theme_list", "1"));
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

        myResources = getResources();
        setArrays();
        fillTable();

    }

    abstract public void setArrays();

    public void fillTable() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<style type=\"text/css\">");
        if (darkTheme) {
            builder.append("body{color: white; background-color: black;}");
        }
        // size font based on the screen width
        builder.append("table{font-size: 5vw}");
        builder.append("td{padding: 1vw}");
        builder.append("</style>");
        builder.append("</head>");
        builder.append("<body>");
        builder.append("<table>");
        builder.append("<tr>");
        int n = meaning.length;
        for(int i = 0; i < n ; i++){
            if (meaning[i].equals("kya")) {
                builder.append("</tr>");
                builder.append("</table>");
                builder.append("<table>");
                builder.append("<tr>");
            }
            builder.append("<td>")
                   .append(japanese[i])
                   .append("</td><td>")
                   .append(meaning[i])
                   .append("</td>");
            if (meaning[i].equals("ya") || meaning[i].equals("yu")) {
                builder.append("<td colspan=\"2\"></td>");
            } else if (meaning[i].equals("wa")) {
                builder.append("<td colspan=\"6\"></td>");
            }
            if (meaning[i].endsWith("o") || meaning[i].equals("n")) {
                builder.append("</tr><tr>");
            }
        }
        builder.append("</tr>");
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
