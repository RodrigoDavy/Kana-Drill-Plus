package com.jorgecastillo.kanadrill;

import android.content.Intent;
import android.os.Bundle;

public class KatakanaTrainingActivity extends TrainingActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void setArrays() {
    meaning = myResources.getStringArray(R.array.romanji);
    japanese = myResources.getStringArray(R.array.katakana);
  }

  @Override
  public void onRestart(){
    super.onRestart();
    finish();
    Intent intent = new Intent(this, KatakanaTrainingActivity.class);
    startActivity(intent);
  }
}
