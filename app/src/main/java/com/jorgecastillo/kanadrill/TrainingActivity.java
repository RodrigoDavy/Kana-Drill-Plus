package com.jorgecastillo.kanadrill;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class TrainingActivity extends EveryActivity {

  protected TextView kanaText, romanjiText;

  protected int count = -1;
  protected List<Integer> order;

  private KanaAudioPlayer kanaAudioPlayer = null;
  protected Resources myResources;

  protected String[] meaning;
  protected String[] japanese;

  protected int[] sounds;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_training);

    kanaAudioPlayer = new KanaAudioPlayer(this);

    kanaText = (TextView) findViewById(R.id.kanaText);
    romanjiText = (TextView) findViewById(R.id.romanjiText);

    myResources = getResources();
    setArrays();

    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    if (myPreferences.getBoolean("setup_true", false)) {
      Collection<String> kanaGroups = myPreferences.getStringSet("kana_groups", Collections.<String>emptySet());

      order = CommonCode.getKanas(kanaGroups);

      if (1 == Integer.parseInt(myPreferences.getString("order_list", "1"))) {
        Collections.shuffle(order);
      } else {
        Collections.sort(order);
      }

      setButtons();
    }

  }

  @Override
  public void onStop() {
    super.onStop();
    kanaAudioPlayer.releaseMediaPlayer();
  }

  abstract public void setArrays();

  public void setButtons() {

    count++;
    if (count >= order.size()) {
      finish();
      return;
    }
    kanaText.setText(japanese[order.get(count)]);
    romanjiText.setText(meaning[order.get(count)]);
    kanaAudioPlayer.play(this, sounds[order.get(count)]);
  }

  public void setButtonsBack() {
    count -= 2;
    if (count < -1) {
      count = -1;
    }
    setButtons();
  }

  @Override
  public boolean onSingleTapUp(MotionEvent event) {
    setButtons();
    return true;
  }

  @Override
  public void rightLeftFling() {
    setButtons();
  }

  @Override
  public void leftRightFling() {
    setButtonsBack();
  }

}
