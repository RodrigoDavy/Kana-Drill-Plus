package com.jorgecastillo.kanadrill;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Arrays;

public class CommonCode {

  public static int theme_list;

  public static void orderLinear(int upto, int order[]) {

    for (int i = 0; i < upto; i++) {
      order[i] = i;
    }

  }

  private static boolean intExists(int[] vector, int element, int length) {

    for (int i = 0; i < length; i++) {
      if (vector[i] == element) {
        return true;
      }
    }

    return false;

  }

  public static void orderRandom(int upto, int order[]) {

    Arrays.fill(order, -1);

    for (int i = 0; i < upto; ) {
      int val = randomInt(upto, -1);
      if (!intExists(order, val, upto)) {
        order[i] = val;
        i++;
      }
    }

  }

  public static int setUpto(int option) {

    int upto = 0;

    switch (option) {
      case 1:
        upto = 5;
        break;
      case 2:
        upto = 10;
        break;
      case 3:
        upto = 15;
        break;
      case 4:
        upto = 20;
        break;
      case 5:
        upto = 25;
        break;
      case 6:
        upto = 30;
        break;
      case 7:
        upto = 35;
        break;
      case 8:
        upto = 38;
        break;
      case 9:
        upto = 46;
        break;
      case 10:
        upto = 71;
        break;
      case 11:
        upto = 92;
        break;
      case 12:
        upto = 107;
        break;
      default:
        break;
    }

    return upto;

  }

  public static int randomInt(int upto, int skip) {

    int number;

    do {
      number = (int) Math.floor(Math.random() * upto);
    } while (number == skip);

    return number;
  }

  public static void intArrayToFile(Context myContext, String filename, int[] array) {
    File root = myContext.getFilesDir();
    File current = new File(root, filename);
    current.delete();
    FileOutputStream outputStream;
    try {
      outputStream = myContext.openFileOutput(filename, Context.MODE_APPEND);
      for (int i : array) {
        String s = "" + i;
        outputStream.write(s.getBytes());
        outputStream.write("\n".getBytes());
      }
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static int[] fileToIntArray(Context myContext, String filename, int size) {

    int[] array = new int[size];
    int i = 0;
    FileInputStream inputStream;
    try {
      int c;
      inputStream = myContext.openFileInput(filename);
      StringWriter writer = new StringWriter();
      while ((c = inputStream.read()) != -1) {
        writer.append((char) c);
      }
      String ints[] = writer.toString().split("\n");
      for (String s : ints) {
        array[i++] = Integer.parseInt(s);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return array;
  }

}
