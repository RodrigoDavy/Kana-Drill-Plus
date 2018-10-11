package com.jorgecastillo.kanadrill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class KanjiTrainingActivity extends EveryActivity implements GoToDialog.Callbacks {

  @Override
  public void goToKanji(int position) {

    if (position > 0 && position < 2137) {
      if (position != 830) {
        count = position - 1;
        setButtons();
      }
    }
  }

  private TextView kanjiText, kanaText, englishText;

  private int count;
  private static final int UPTO = 2136;
  private int[] order;

  private Resources myResources;

  private String[] kanji;
  private String[] english;
  private String[] kana;

  private static final String KANJI_BOOKMARK = "kanji_bookmar";
  private boolean autoforward = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_training_kanji);

    kanjiText = (TextView) findViewById(R.id.kanjiText);
    englishText = (TextView) findViewById(R.id.englishText);
    kanaText = (TextView) findViewById(R.id.kanaText);

    myResources = getResources();
    kanji = myResources.getStringArray(R.array.kanji);
    english = myResources.getStringArray(R.array.english);
    kana = myResources.getStringArray(R.array.kana);

    order = new int[2136];
    try {
      FileInputStream input = openFileInput(KANJI_BOOKMARK);
      String count_bookmark = new Scanner(input).useDelimiter("\\Z").next();
      input.close();
      count = Integer.parseInt(count_bookmark);
    } catch (FileNotFoundException fnfe) {} catch (Exception e) {
      e.printStackTrace();
    }
    CommonCode.orderLinear(UPTO, order);
    kanjiText.setText(kanji[order[count]]);
    englishText.setText(english[order[count]]);
    kanaText.setText(kana[order[count]]);
    getActionBar().setTitle("" + (count + 1));

    if (myPreferences.getBoolean("setup_true", false)) {
      autoforward_speed = Integer.parseInt(myPreferences.getString("autoforward_speed", "1"));
    }

  }

  @Override
  public void rightLeftFling() {

    count++;
    if (count == 829) {
      count = 830;
    }
    if (count >= UPTO) {
      finish();
      return;
    }

    setButtons();
  }

  public void setButtons() {

    kanjiText.setText(kanji[order[count]]);
    englishText.setText(english[order[count]]);
    kanaText.setText(kana[order[count]]);
    getActionBar().setTitle("" + (count + 1));
  }

  @Override
  public void leftRightFling() {

    count--;
    if (count == 829) {
      count = 828;
    }

    if (count < 0) {
      count = 0;
    }

    setButtons();
  }

  @Override
  public boolean onSingleTapUp(MotionEvent event) {
    rightLeftFling();
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.training, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    Intent intent;
    switch (id) {
      case R.id.action_bookmark:
        String filename = KANJI_BOOKMARK;
        String count_bookmark = "" + count;
        FileOutputStream outputStream;
        Context mContext = getApplicationContext();
        try {
          outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
          outputStream.write(count_bookmark.getBytes());
          outputStream.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      case R.id.action_go_to:
        GoToDialog dialog = new GoToDialog();
        dialog.setTitle("Choose a Kanji");
        dialog.show(getFragmentManager(), "Go To Dialog");
        break;
      case R.id.action_auto_forward:
        autoforward = !autoforward;
        if (autoforward) {
          final Activity mactivity = this;
          new Thread(new Runnable() {
            @Override
            public void run() {
              while (autoforward) {
                mactivity.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                    count++;
                    if (count == 829) {
                      count = 830;
                    }
                    if (count == (UPTO - 1)) {
                      autoforward = false;
                    }
                    setButtons();
                  }
                });
                try {
                  Thread.sleep(autoforward_speed * 1000);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          }).start();
        }
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onRestart(){
    super.onRestart();
    finish();
    Intent intent = new Intent(this, KanjiTrainingActivity.class);
    startActivity(intent);
  }
}
