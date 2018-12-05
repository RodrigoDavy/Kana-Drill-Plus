package com.jorgecastillo.kanadrill;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommonCode {
  private CommonCode() {
    throw new AssertionError();
  }

  public static int theme_list;

  public static void orderLinear(int upto, int[] order) {

    for (int i = 0; i < upto; i++) {
      order[i] = i;
    }

  }

  private static void addRange(Collection<Integer> collection, int start, int stop) {
    for (int i = start; i < stop; ++i) {
      collection.add(i);
    }
  }

  public static List<Integer> getKanas(Collection<String> kanaGroups) {
    List<Integer> kanas = new ArrayList<Integer>();
    for (String kanaGroup : kanaGroups) {
      int group = Integer.parseInt(kanaGroup);
      switch (Integer.parseInt(kanaGroup)) {
      case 1:
        addRange(kanas, 0, 5);
        break;
      case 2:
        addRange(kanas, 5, 10);
        break;
      case 3:
        addRange(kanas, 10, 15);
        break;
      case 4:
        addRange(kanas, 15, 20);
        break;
      case 5:
        addRange(kanas, 20, 25);
        break;
      case 6:
        addRange(kanas, 25, 30);
        break;
      case 7:
        addRange(kanas, 30, 35);
        break;
      case 8:
        addRange(kanas, 35, 38);
        break;
      case 9:
        addRange(kanas, 38, 46);
        break;
      case 10:
        addRange(kanas, 46, 71);
        break;
      case 11:
        addRange(kanas, 71, 92);
        break;
      case 12:
        addRange(kanas, 92, 107);
        break;
      default:
        throw new IllegalStateException("unexpected group: " + group);
      }
    }
    return kanas;
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
      String[] ints = writer.toString().split("\n");
      for (String s : ints) {
        array[i++] = Integer.parseInt(s);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return array;
  }

  public static void addVocabularyFile(Resources resources, String fileName, Collection<String> japaneses, Collection<String> meanings) throws IOException {
      BufferedReader reader = null;
      try {
          reader = new BufferedReader(new InputStreamReader(resources.getAssets().open(fileName), StandardCharsets.UTF_8));
          while (true) {
              String line = reader.readLine();
              if (line == null) {
                  break;
              } else if (line.isEmpty()) {
                  continue;
              }
              String[] parts = line.split(",");
              if (parts.length != 2) {
                  Log.i("KanaDrill", "malformed CSV: " + line);
                  continue;
              }
              japaneses.add(parts[0]);
              meanings.add(parts[1].trim());
          }
      } finally {
          if (reader != null) {
              try {
                  reader.close();
              } catch (IOException ioe) {
                  // ignore
              }
          }
      }
  }
}
