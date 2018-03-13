package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class OfferDisplayActivity extends Activity {
    private static final String LOG=OfferDisplayActivity.class.getName();

    private ImageView photoImageView;
    private File photoFile;
    private final Handler handler = new Handler();
    private TextView titleDisplayField;
    private TextView bestBeforeDateDisplayField;
    private TextView longDescriptionDisplayField;
    private TextView dateAddedDisplayField;

    private TextView categoriesDisplayField;

    private Button editOfferButton;
    private Button showContactInformationButton;
    private Offer currentOffer;
    private int offererID=-1;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_display);

        registerViews();


        currentOffer = new Offer(OfferDisplayActivity.this);
        currentOffer.setID(getIntent().getIntExtra(Constants.keyOfferID, -1));
        Log.i(LOG, Constants.OFFER_ID + currentOffer.getID());

        currentUser = new User(this);


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        new RetrieveOfferInfoTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_offer, menu);
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
        titleDisplayField = (TextView) findViewById(R.id.offerDisplayTitle);
        bestBeforeDateDisplayField = (TextView) findViewById(R.id.offer_display_best_before_tv);
        longDescriptionDisplayField = (TextView) findViewById(R.id.detailed_description_tv);
        dateAddedDisplayField = (TextView) findViewById(R.id.offerDisplayDateAdded);

        categoriesDisplayField = (TextView) findViewById(R.id.offerDisplayCategories);

        photoImageView = (ImageView) findViewById(R.id.offerDisplayPicture);


        editOfferButton=(Button)findViewById(R.id.offerDisplayEditOfferButton);
        showContactInformationButton=(Button)findViewById(R.id.offerDisplayShowContactInformationButton);

    }

    public void editOffer(View view){
        Intent intent = new Intent(getApplicationContext(), OfferEditActivity.class);
        intent.putExtra(Constants.keyOfferID, currentOffer.getID());
        startActivity(intent);
    }


    public void showContactInfo(View view)
    {
        //Intent intent = new Intent(OfferDisplayActivity.this, ProfileDisplayActivity.class);
        Intent intent = new Intent(getApplicationContext(), ProfileDisplayActivity.class);
        intent.putExtra(Constants.keyUserID, offererID);
        startActivity(intent);
    }

    public void cancelButtonClicked(View view)
    {
        finish();
    }


    public void imageViewClicked(View view)
    {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(photoFile), "image/*");
            startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.imageViewerStartNotSuccessful) + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }


    }


    private class RetrieveOfferInfoTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred=false;
        private String errorMessage="";
        private ProgressDialog progressDialog;


        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(OfferDisplayActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getApplicationContext().getString(R.string.offerDataFetchingInProgress));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... params)
        {
            if (!currentOffer.fillObjectFromDatabase()) {
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.offerDataFetchingNotSuccessful) + currentOffer.getErrorMessage();
                Log.e(LOG, errorMessage);
                return null;
            }

            photoFile=currentOffer.getPicture();

            /*
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("tid", String.valueOf(currentOffer.getTransactID())));

            JSONParser jsonParser = new JSONParser();
            JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(getApplicationContext()) + "/" + "get_transaction_details.php", Constants.JSON_GET, nameValuePairs);

            if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            {
                errorOccurred=true;
                errorMessage="Could not retrieve offerer ID!";
                return null;

            }

            JSONArray transactionJSONArray=returnObject.optJSONArray("transaction");
            JSONObject transactionJSONObject=transactionJSONArray.optJSONObject(0);
            if (transactionJSONObject == null)
            {
                errorOccurred=true;
                errorMessage="Could not retrieve offerer ID!";
                return null;
            }

            */
            try { offererID=currentOffer.getOffererID(); }
            catch (Exception e) {
                errorOccurred=true;
                errorMessage="Could not retrieve offerer ID!";
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

            titleDisplayField.setText(currentOffer.getShortDescription());
            longDescriptionDisplayField.setText(currentOffer.getLongDescription());
            bestBeforeDateDisplayField.setText(String.format("%tF", currentOffer.getMhd()));
            dateAddedDisplayField.setText(String.format("%1$tF %1$tT", currentOffer.getDateAdded()));
            if (photoFile != null) {
                photoImageView.setImageURI(null);
                photoImageView.setImageURI(Uri.fromFile(photoFile));
            }
            if (offererID == currentUser.getID())
            {
                editOfferButton.setVisibility(View.VISIBLE);
                showContactInformationButton.setVisibility(View.INVISIBLE);
            }

            categoriesDisplayField.setText(currentOffer.getCategories().keySet().toString());

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.offerDataFetchingSuccessful), Toast.LENGTH_LONG).show();

            Log.i(LOG, "Offerer ID  " + offererID);
        }
    }

}
