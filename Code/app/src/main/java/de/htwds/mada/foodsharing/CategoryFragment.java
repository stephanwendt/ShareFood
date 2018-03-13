package de.htwds.mada.foodsharing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class CategoryFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] values = Constants.CATEGORIES_ARRAY;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Constants.CATEGORY_WORD)
               .setItems(values, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //EditText et = (EditText) getActivity().findViewById(R.id.offer_category_edit);
                       TextView et = (TextView) getActivity().findViewById(R.id.offer_category_edit);
                       et.setText(values[which]);
                   }
               });
        return builder.create();
    }
}
