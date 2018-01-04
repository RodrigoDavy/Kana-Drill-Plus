package com.jorgecastillo.kanadrill;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class EveryActivity extends Activity implements GestureDetector.OnGestureListener {

  protected GestureDetector mDetector;
  protected SharedPreferences myPreferences;
  public int theme_list;
  public int autoforward_speed = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    if (myPreferences.getBoolean("setup_true", false)) {
      theme_list = Integer.parseInt(myPreferences.getString("theme_list", "1"));
      CommonCode.theme_list = theme_list;
      if (theme_list == 1) {
        setTheme(R.style.HoloLight);
      } else if (theme_list == 2) {
        setTheme(R.style.HoloDark);
      } else if (theme_list == 3) {
        setTheme(R.style.MaterialLight);
      } else if (theme_list == 4) {
        setTheme(R.style.MaterialDark);
      }

    }
    mDetector = new GestureDetector(this, this);
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

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    this.mDetector.onTouchEvent(event);
    return super.onTouchEvent(event);
  }

  @Override
  public boolean onDown(MotionEvent event) { return true; }

  @Override
  public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {

    float startX, startY, endX, endY;
    startX = event1.getX();
    startY = event1.getY();
    endX = event2.getX();
    endY = event2.getY();
    if (endX < startX) {
      rightLeftFling();
    } else if (endX > startX) {
      leftRightFling();
    }
    return true;
  }

  @Override
  public void onLongPress(MotionEvent event) {}

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    return true;
  }

  @Override
  public void onShowPress(MotionEvent event) {}

  @Override
  public boolean onSingleTapUp(MotionEvent event) { return true; }

  public void rightLeftFling() {}

  public void leftRightFling() {}

}
