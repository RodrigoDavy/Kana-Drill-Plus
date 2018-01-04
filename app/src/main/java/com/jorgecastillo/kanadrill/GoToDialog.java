package com.jorgecastillo.kanadrill;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class GoToDialog extends DialogFragment {

  EditText kanjinumber;
  String title = "";
  Callbacks mListener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.gotodialog, null);
    kanjinumber = (EditText) dialogView.findViewById(R.id.textoGuardarComo);
    builder.setView(dialogView);

    builder.setTitle(title).setMessage(R.string.question_goto)
        .setPositiveButton(R.string.input_goto, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            int number;
            try {
              number = Integer.parseInt(kanjinumber.getText().toString());

            } catch (Exception e) {
              number = 0;
            }
            mListener.goToKanji(number);
          }
        })
        .setNegativeButton(R.string.input_cancel, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // User cancelled the dialog
          }
        });

    return builder.create();
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (Callbacks) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " Must implement callbacks!");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface Callbacks {
    public void goToKanji(int position);
  }
}
