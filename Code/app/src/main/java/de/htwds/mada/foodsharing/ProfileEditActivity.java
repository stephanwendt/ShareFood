package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Edits the profile of a given user (specified via intent)
 * or creates a new one.
 */
public class ProfileEditActivity extends Activity {
    private static final String LOG=ProfileEditActivity.class.getName();

    private EditText emailInputField;
    private EditText passwordInputField;
    private EditText usernameInputField;
    private EditText firstNameInputField;
    private EditText lastNameInputField;
    //private EditText phoneInputField;
    private EditText cityInputField;
    private EditText streetInputField;
    private EditText houseNumberInputField;
    private EditText zipcodeInputField;
    private EditText countryInputField;
    private EditText additionalInputField;

    private Button profileEditSaveButton;

    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        registerViews();


        Intent intent=getIntent();
        boolean createNewProfile=intent.getBooleanExtra(Constants.NEW_PROFILE, false);
        if (createNewProfile)
        {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(Constants.currentUserIdKey, -1).apply();
        }
        loggedInUser = new User(this);


        if (!createNewProfile) {
            loggedInUser.setEdited(true);
            new RetrieveProfileInfoTask().execute();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
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

    private void registerViews()
    {
        profileEditSaveButton=(Button)findViewById(R.id.profileEditSaveButton);

        emailInputField=(EditText) findViewById(R.id.profileEditEmail);
        passwordInputField=(EditText) findViewById(R.id.profileEditPassword);
        usernameInputField=(EditText) findViewById(R.id.profileEditUsername);
        firstNameInputField = (EditText) findViewById(R.id.profileEditFirstName);
        lastNameInputField = (EditText) findViewById(R.id.profileEditLastName);
        //phoneInputField=(EditText) findViewById(R.id.profileEditPhone);
        streetInputField=(EditText) findViewById(R.id.profileEditStreet);
        houseNumberInputField=(EditText) findViewById(R.id.profileEditHouseNumber);
        zipcodeInputField=(EditText) findViewById(R.id.profileEditZipcode);
        cityInputField=(EditText) findViewById(R.id.profileEditCity);
        countryInputField=(EditText) findViewById(R.id.profileEditCountry);
        additionalInputField=(EditText) findViewById(R.id.profileEditAdditional);
    }

    public void buttonClicked(View view){
        Button btn = (Button) view;
        switch (btn.getId()) {
//            case R.id.profileEditCancelButton:
//                finish();
//                break;
            case R.id.profile_edit_offer_btn:
                startActivity(new Intent(ProfileEditActivity.this, OfferEditActivity.class));
                break;
            case R.id.profileEditSaveButton:
                updateProfile();
                break;

            default:
        }
    }


    private void updateProfile()
    {
        if (!formToObject()) return;
        new SaveProfileTask().execute();

    }

    /**
     * Sets the user object's attributes from the contents of the form fields
     */
    private boolean formToObject()
    {
        boolean formCorrectlyFilled=true;
        View firstWrongField=null;

        try { loggedInUser.setEmail(emailInputField.getText().toString().trim()); }
        catch (Exception e) { emailInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=emailInputField; }

        try {
            int passwordLength = passwordInputField.length();
            char[] password = new char[passwordLength];
            passwordInputField.getText().getChars(0, passwordLength, password, 0);
            loggedInUser.setPassword(password);
        }
        catch (Exception e) { passwordInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=passwordInputField; }

        try { loggedInUser.setUsername(usernameInputField.getText().toString().trim()); }
        catch (Exception e) { usernameInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=usernameInputField; }

        try { loggedInUser.setVorname(firstNameInputField.getText().toString().trim()); }
        catch (Exception e) { firstNameInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=firstNameInputField; }

        try { loggedInUser.setNachname(lastNameInputField.getText().toString().trim()); }
        catch (Exception e) { lastNameInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=lastNameInputField; }

        try { loggedInUser.setStreet(streetInputField.getText().toString().trim()); }
        catch (Exception e) { streetInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=streetInputField; }

        try { loggedInUser.setHouseNumber(houseNumberInputField.getText().toString().trim()); }
        catch (Exception e) { houseNumberInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=houseNumberInputField; }

        try { loggedInUser.setPlz(zipcodeInputField.getText().toString().trim()); }
        catch (Exception e) { zipcodeInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=zipcodeInputField; }

        try { loggedInUser.setCity(cityInputField.getText().toString().trim()); }
        catch (Exception e) { cityInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=cityInputField; }

        try { loggedInUser.setCountry(countryInputField.getText().toString().trim()); }
        catch (Exception e) { countryInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=countryInputField; }

        try { loggedInUser.setAdditional(additionalInputField.getText().toString().trim());}
        catch (Exception e) { additionalInputField.setError(e.getLocalizedMessage()); formCorrectlyFilled=false;  if (firstWrongField == null) firstWrongField=additionalInputField; }

        if (firstWrongField != null)
            firstWrongField.requestFocus();

        return formCorrectlyFilled;
    }


    private class RetrieveProfileInfoTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred=false;
        private String errorMessage="";
        private ProgressDialog progressDialog;


        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(ProfileEditActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getApplicationContext().getString(R.string.profileDataFetchingInProgress));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... params)
        {
            if (!loggedInUser.fillObjectFromDatabase())
            {
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.profileDataFetchingNotSuccessful) + loggedInUser.getErrorMessage();
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

            emailInputField.setText(loggedInUser.getEmail());
            passwordInputField.setText(String.valueOf(loggedInUser.getPassword()));
            usernameInputField.setText(loggedInUser.getUsername());
            firstNameInputField.setText(loggedInUser.getVorname());
            lastNameInputField.setText(loggedInUser.getNachname());
            //phoneInputField.setText(currentUser.getPhoneNumber());
            streetInputField.setText(loggedInUser.getStreet());
            houseNumberInputField.setText(loggedInUser.getHouseNumber());
            zipcodeInputField.setText(String.valueOf(loggedInUser.getPlz()));
            cityInputField.setText(loggedInUser.getCity());
            countryInputField.setText(loggedInUser.getCountry());
            additionalInputField.setText(loggedInUser.getAdditional());

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.userDataFetchingSuccessful), Toast.LENGTH_LONG).show();
        }
    }


    private class SaveProfileTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred=false;
        private String errorMessage="";
        private ProgressDialog progressDialog;


        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(ProfileEditActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getApplicationContext().getString(R.string.profileUpdatingInProgress));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... params)
        {

            if (!loggedInUser.saveObjectToDatabase())
            {
                Log.e(LOG, loggedInUser.getErrorMessage());
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.profileUpdatingNotSuccessful) + loggedInUser.getErrorMessage();
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

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.profileUpdatingSuccessful), Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
