package de.htwds.mada.foodsharing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


// Asks user to register
public class BrowserDialogFragment extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(Constants.REGISTER_ACCOUNT)
//                    .setMessage("Do you want to register an account with us?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing (will close dialog)
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                            intent.putExtra(Constants.NEW_PROFILE, true);
                            startActivityForResult(intent, 0);
                        }
                    })
                    .create();
        }}
