package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class BrowseCreateEditActivity extends Activity { // implements AdapterView.OnItemSelectedListener {
    private static final String LOG=BrowseCreateEditActivity.class.getName();
    //private Spinner spinner;
    //private View browseLayout;
    private ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_create_edit_new);
        //spinner = (android.widget.Spinner) findViewById(R.id.browse_spinner);
        //browseLayout = findViewById(R.id.browse_layout);


        Log.i(LOG, "Server: " + Constants.getHttpBaseUrl(this));

        /* if you want to use an array adapter instead of a button */
        /*ArrayAdapter<CharSequence> adapter;
        if (Locale.getDefault().getDisplayLanguage().equals("Deutsch")) {
            // Creates an ArrayAdapter using the string array and customized spinner layout
            // German language
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.items_array_german, R.layout.spinner_layout);
        } else {
            // default language English
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.items_array_english, R.layout.spinner_layout);
        }
        // Adds customised layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_items_layout);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        // Add new progress bar
        progress = new ProgressDialog(this);*/
    }
       /*
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getSelectedItemPosition()) {

                case 0:
                     break;

                case 1:
//                    tests if user input is empty
//                    testInput(view);

                    //shows progress bar
                    showProgress(view);
                    //fills intent with params
                    fillIntent(ResultActivity.class);
                    break;

                case 2:
//                    testInput(view);

                    //shows progress bar
                    showProgress(view);
                    //fills intent with params
                    fillIntent(ResultActivity.class);
                    break;

                case 3:
//                    testInput(view);

                    //shows progress bar
                    showProgress(view);
                    //fills intent with params
                    fillIntent(ResultActivity.class);
                    break;

                case 4:
//                    testInput(view);
                    //shows progress bar
                    showProgress(view);
                    //fills intent with params
                    fillIntent(ResultActivity.class);
                    break;

                default:
                    break;
            }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void buttonClicked(View view) {
        Button btn = (Button) view;

                //switch to pass an intent for distinct search
                switch (btn.getId()) {
                    //opens offering browser
                    case R.id.browseCreateEditBrowseButton:
                        fillIntent(ResultActivity.class);
                        break;
                    // Opens OfferEdit to create new offer
                    case R.id.browseCreateEditNewOfferButton:
                        fillIntent(OfferEditActivity.class);
                        break;
                    // Opens EditSearch to enter offer which will be edited
                    case R.id.browseCreateEditEditMyOffersButton:
                        Intent intent=new Intent(getApplicationContext(), ResultActivity.class);
                        intent.putExtra(Constants.RESULTS_FILTERED_BY_USER, true);
                        startActivity(intent);
                        //fillIntent(EditSearchActivity.class);
                        break;
                    // Opens EditSearch to enter wanted profile
                    case R.id.browseCreateEditEditProfileButton:
                        fillIntent(ProfileEditActivity.class);
                        break;
                    //
//                    case R.id.browseCreateEditTransactionHistoryButton:
//                        fillIntent(TransactionHistoryActivity.class);
//                        break;
                    case R.id.browseCreateEditExitButton:

                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.USER_ID, 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        moveTaskToBack(true);
                        break;
                    default:
                        break;
                }
            }

    void fillIntent(Class activity) {
        Intent i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }

    private void testInput(View view){
        EditText et = (EditText) view;

        String input = et.getText().toString().trim();
        if (input.isEmpty()){
            Toast.makeText(getApplicationContext(), Constants.NO_ARGUMENT, Toast.LENGTH_LONG).show();
        }
        showProgress(view);
        Toast.makeText(getApplicationContext(), Constants.WAIT_INFO, Toast.LENGTH_LONG).show();

    }

    void showProgress(View view){
        progress.setMessage(Constants.PLEASE_WAIT);
        progress.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.show();

        final int totalProgressTime = 600;

        final Thread t = new Thread(){

            @Override
            public void run(){

                int jumpTime = 0;
                while(jumpTime < totalProgressTime){
                    try {
                        jumpTime ++;
                        progress.setProgress(jumpTime);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        };
        t.start();

    }
}

