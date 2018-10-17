package com.jorgecastillo.kanadrill;

import android.os.Bundle;

public class HiraganaTrainingActivity extends TrainingActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

  @Override
  public void setArrays() {
    meaning = myResources.getStringArray(R.array.romanji);
    japanese = myResources.getStringArray(R.array.hiragana);
    sounds = new int[]{R.raw.a,R.raw.i,R.raw.u,R.raw.e,R.raw.o,
            R.raw.ka,R.raw.ki,R.raw.ku,R.raw.ke,R.raw.ko,
            R.raw.sa,R.raw.shi,R.raw.su,R.raw.se,R.raw.so,
            R.raw.ta,R.raw.chi,R.raw.tsu,R.raw.te,R.raw.to,
            R.raw.na,R.raw.ni,R.raw.nu,R.raw.ne,R.raw.no,
            R.raw.ha,R.raw.hi,R.raw.fu,R.raw.he,R.raw.ho,
            R.raw.ma,R.raw.mi,R.raw.mu,R.raw.me,R.raw.mo,
            R.raw.ma,R.raw.mi,R.raw.mu,R.raw.me,R.raw.mo,
            R.raw.ya,R.raw.yu,R.raw.yo,
            R.raw.ra,R.raw.ri,R.raw.ru,R.raw.re,R.raw.ro,
            R.raw.wa,R.raw.o,R.raw.n};
  }
}
