package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Displays the profile of a given user
 */

public class ProfileDisplayActivity extends Activity {
    private static final String LOG=ProfileDisplayActivity.class.getName();

    private TextView emailDisplayField;
    private TextView userNameDisplayField;
    private TextView firstNameDisplayField;
    private TextView lastNameDisplayField;
    //private TextView phoneDisplayField;
    private TextView streetDisplayField;
    private TextView houseNumberDisplayField;
    private TextView zipcodeDisplayField;
    private TextView cityDisplayField;
    private TextView countryDisplayField;
    private TextView additionalDisplayField;

    private Button profileEditButton;

    private User displayedUser, currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        registerViews();

        displayedUser = new User(this, getIntent().getIntExtra(Constants.keyUserID, -1));
        currentUser = new User(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        profileEditButton.setVisibility(View.INVISIBLE);

        new RetrieveProfileInfoTask().execute();
    }

    private void registerViews()
    {
        emailDisplayField = (TextView) findViewById(R.id.profileEditEmail);
        userNameDisplayField = (TextView) findViewById(R.id.profile_display_username_tv);
        firstNameDisplayField = (TextView) findViewById(R.id.profile_edit_first_name_tv);
        lastNameDisplayField = (TextView) findViewById(R.id.profile_edit_last_name_tv);
        //phoneDisplayField = (TextView) findViewById(R.id.profileEditPhone);
        streetDisplayField = (TextView) findViewById(R.id.profileEditStreet);
        houseNumberDisplayField = (TextView) findViewById(R.id.profileEditHouseNumber);
        zipcodeDisplayField = (TextView) findViewById(R.id.profileEditZipcode);
        cityDisplayField = (TextView) findViewById(R.id.profileEditCity);
        countryDisplayField = (TextView) findViewById(R.id.profile_displ_country_tv);
        additionalDisplayField=(TextView) findViewById(R.id.profileDisplayAdditional);

        profileEditButton = (Button) findViewById(R.id.profileDisplayEditButton);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_display, menu);
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

    public void buttonClicked(View view){
        Button btn = (Button) view;
        switch (btn.getId()) {

            case R.id.profileDisplayEditButton:
                Intent intent=new Intent(ProfileDisplayActivity.this, ProfileEditActivity.class);
                startActivity(intent);
                break;
//            case R.id.profile_edit_show_tr_history:
//                fillIntent(TransactionHistoryActivity.class);
//                break;
            default:
        }
    }

    void fillIntent(Class activity){
        Intent i;
        i = new Intent(getApplicationContext(), activity);
        startActivity(i);
    }

    private class RetrieveProfileInfoTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred=false;
        private String errorMessage="";
        private ProgressDialog progressDialog;


        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(ProfileDisplayActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getApplicationContext().getString(R.string.userDataFetchingInProgress));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... params)
        {
            if (!displayedUser.fillObjectFromDatabase())
            {
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.userDataFetchingNotSuccessful) + displayedUser.getErrorMessage();
                Log.e(LOG, errorMessage);
                return null;

            }

            return null;
        }


        protected void onPostExecute(Void param)
        {
            if (errorOccurred) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                return;
            }

            emailDisplayField.setText(displayedUser.getEmail());
            userNameDisplayField.setText(displayedUser.getUsername());
            firstNameDisplayField.setText(displayedUser.getVorname());
            lastNameDisplayField.setText(displayedUser.getNachname());
            streetDisplayField.setText(displayedUser.getStreet());
            houseNumberDisplayField.setText(displayedUser.getHouseNumber());
            zipcodeDisplayField.setText(String.valueOf(displayedUser.getPlz()));
            cityDisplayField.setText(displayedUser.getCity());
            countryDisplayField.setText(displayedUser.getCountry());
            additionalDisplayField.setText(currentUser.getAdditional());

            Log.i(LOG, "Displayed user: " + displayedUser.getID() + "Current user: " + currentUser.getID());
            if (displayedUser.getID() == currentUser.getID())
            {
                profileEditButton.setVisibility(View.VISIBLE);
            }
            progressDialog.dismiss();
            //Toast.makeText(getApplicationContext(), Constants.USER_FETCHED, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.userDataFetchingSuccessful), Toast.LENGTH_LONG).show();
        }
    }
}
