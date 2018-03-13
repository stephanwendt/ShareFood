package de.htwds.mada.foodsharing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Lists all offers in the database and provides the functionality to sort and filter them
 */

public class ResultActivity extends Activity {
    private static final String LOG=ResultActivity.class.getName();
    //private ArrayAdapter<Offer> offerArrayAdapter;
    private OfferArrayAdapter<Offer> offerArrayAdapter;
    private ListView resultListView;

    private Spinner sortSpinner;
    private EditText filterInputField;
    private EditText categoriesFilterInputField;
    private ArrayAdapter<String> spinnerAdapter;

    private HashMap<String,Integer> categories;

    private ArrayList<Offer> allOffers;

    boolean showOffersOnlyForGivenUser=false;
    User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        categories=new HashMap<String,Integer>();
        allOffers=new ArrayList<Offer>();

        sortSpinner=(Spinner) findViewById(R.id.resultActivitySortSpinner);
        filterInputField=(EditText) findViewById(R.id.resultActivityFilter);
        categoriesFilterInputField=(EditText) findViewById(R.id.resultActivityCategoriesFilter);

        resultListView=(ListView) findViewById(R.id.activity_result_listview);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Offer offer = (Offer) parent.getItemAtPosition(position);
                Log.i(LOG, Constants.OFFER_ID + offer.getID());
                Intent intent = new Intent(ResultActivity.this, OfferDisplayActivity.class);
                intent.putExtra(Constants.keyOfferID, offer.getID());
                startActivity(intent);
            }
        });


        spinnerAdapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        //spinnerAdapter.add("Not sorted");
                    /*
                    case "Not sorted":
                        //offerArrayAdapter=new ArrayAdapter<>(ResultActivity.this, android.R.layout.simple_list_item_1);
                        //new RetrieveOffersTask().execute();
                        offerArrayAdapter.sort(null);
                        break;
                        */
        spinnerAdapter.add("Short Description");
        spinnerAdapter.add("Long Description");
        spinnerAdapter.add("Best Before Date");
        spinnerAdapter.add("Date Added");
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectionString=(String)parent.getItemAtPosition(position);
                final Collator collator=Collator.getInstance();
                switch(selectionString)
                {
                    case "Short Description":
                        Log.i(LOG, "Short Description");
                        offerArrayAdapter.sort(new Comparator<Offer>() {
                            @Override
                            public int compare(Offer lhs, Offer rhs) {
                                return collator.compare(lhs.getShortDescription(), rhs.getShortDescription());
                            }
                        });
                        break;
                    case "Long Description":
                        Log.i(LOG, "Long Description");
                        offerArrayAdapter.sort(new Comparator<Offer>() {
                            @Override
                            public int compare(Offer lhs, Offer rhs) {
                                return collator.compare(lhs.getLongDescription(), rhs.getLongDescription());
                            }
                        });
                        break;
                    case "Best Before Date":
                        Log.i(LOG, "Best Before Date");
                        offerArrayAdapter.sort(new Comparator<Offer>() {
                            @Override
                            public int compare(Offer lhs, Offer rhs) {
                                return lhs.getMhd().compareTo(rhs.getMhd());
                            }
                        });
                        break;
                    case "Date Added":
                        Log.i(LOG, "Date Added");
                        offerArrayAdapter.sort(new Comparator<Offer>() {
                            @Override
                            public int compare(Offer lhs, Offer rhs) {
                                return lhs.getDateAdded().compareTo(rhs.getDateAdded());
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Log.i(LOG, "Nothing selected.");
            }
        });

        filterInputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                ResultActivity.this.offerArrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        categoriesFilterInputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //Log.i(LOG, "Hallo " + cs + categories.keySet().toString());
                if (cs.toString().isEmpty())
                {
                    offerArrayAdapter=new OfferArrayAdapter<>(ResultActivity.this, android.R.layout.simple_list_item_1);
                    offerArrayAdapter.addAll(allOffers);
                    offerArrayAdapter.notifyDataSetChanged();
                    resultListView.setAdapter(offerArrayAdapter);
                    return;
                }
                if (categories.containsKey(cs.toString()))
                {
                    //Log.i(LOG, "Drin " + cs);
                    offerArrayAdapter=new OfferArrayAdapter<>(ResultActivity.this, android.R.layout.simple_list_item_1);
                    for (Offer offer : allOffers)
                    {
                        //Log.i(LOG, "Offer " + cs + " " + offer.getShortDescription() + offer.getCategories().keySet().toString());
                        if (offer.getCategories().containsKey(cs.toString()))
                        {
                            //Log.i(LOG, "Offer gefunden " + cs + " " + offer.getShortDescription());
                            offerArrayAdapter.add(offer);
                        }
                    }
                    offerArrayAdapter.notifyDataSetChanged();
                    resultListView.setAdapter(offerArrayAdapter);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        //registerViews();

        Intent intent=getIntent();
        showOffersOnlyForGivenUser=intent.getBooleanExtra(Constants.RESULTS_FILTERED_BY_USER, false);
        if (showOffersOnlyForGivenUser)
        {
            loggedInUser=new User(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        categories.clear();
        allOffers.clear();
        offerArrayAdapter=new OfferArrayAdapter<>(ResultActivity.this, android.R.layout.simple_list_item_1);
        new RetrieveOffersTask().execute();
        //new RetrieveCategoriesTask().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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


    private class RetrieveOffersTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred=false;
        private String errorMessage="";
        private ProgressDialog progressDialog;


        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(ResultActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getApplicationContext().getString(R.string.offersDataFetchingInProgress));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... params)
        {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

            JSONParser jsonParser = new JSONParser();
            JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(getApplicationContext()) + "/" + "get_categories.php", Constants.JSON_GET, nameValuePairs);


            if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) {
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.categoriesFetchingNotSuccessful) + returnObject.optString(Constants.MESSAGE_WORD);
                return null;
            }

            JSONArray categoriesJSONArray =returnObject.optJSONArray("categories");
            if (categoriesJSONArray == null)
            {
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.categoriesFetchingNotSuccessful);
                return null;
            }


            String categoryName;
            int categoryID;
            JSONObject categoryJSONObject;
            categories.clear();
            for (int i=0; i< categoriesJSONArray.length(); i++) {
                categoryJSONObject = categoriesJSONArray.optJSONObject(i);
                if (categoryJSONObject == null) {
                    errorOccurred=true;
                    errorMessage="Could not retrieve category " + i;
                    return null;
                }

                categoryName= categoryJSONObject.optString("name", "");
                categoryID= categoryJSONObject.optInt("id", -1);
                // TODO:
                if (categoryName.isEmpty() || categoryID == -1) {
                    errorOccurred = true;
                    errorMessage = "Could not retrieve category " + i;
                    return null;
                }
                categories.put(categoryName, categoryID);
            }


            //ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair(Constants.JSON_TRANS_ID,Constants.BLA_WORD));

            //JSONParser jsonParser = new JSONParser();
            jsonParser = new JSONParser();
            //JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(getApplicationContext()) + "/" + Constants.URL_GET_ALL_OFFERS, Constants.JSON_GET, nameValuePairs);
            returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(getApplicationContext()) + "/" + Constants.URL_GET_ALL_OFFERS, Constants.JSON_GET, nameValuePairs);


            if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) {
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.offersDataFetchingNotSuccessful);
                return null;
            }

            JSONArray offerJSONArray=returnObject.optJSONArray(Constants.OFFERS_WORD);
            if (offerJSONArray == null)
            {
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.offersDataFetchingNotSuccessful);
                return null;
            }
            JSONObject offerJSONObject;
            for (int i=0; i<offerJSONArray.length(); i++) {
                offerJSONObject = offerJSONArray.optJSONObject(i);
                if (offerJSONObject != null) {
                    Offer offer=new Offer(ResultActivity.this, offerJSONObject);
                    if (showOffersOnlyForGivenUser)
                    {
                        try { if (loggedInUser.getID() != offer.getOffererID()) continue; }
                        catch (Exception e) {
                            Log.e(LOG, "Could not retrieve offererID!");
                            continue;
                        }
                    }
                    offer.fillObjectFromDatabase();
                    allOffers.add(offer);
                    offerArrayAdapter.add(offer);
                }
                else
                {
                    errorOccurred=true;
                    errorMessage="Could not retrieve offer info " + i;
                    return null;
                }
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

            categoriesFilterInputField.setHint(categories.keySet().toString());

            offerArrayAdapter.notifyDataSetChanged();
            resultListView.setAdapter(offerArrayAdapter);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.offersDataFetchingSuccessful), Toast.LENGTH_LONG).show();
        }
    }


    private class RetrieveCategoriesTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred=false;
        private String errorMessage="";
        private ProgressDialog progressDialog;


        protected void onPreExecute()
        {
            progressDialog=new ProgressDialog(ResultActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getApplicationContext().getString(R.string.categoriesFetchingInProgress));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... params)
        {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

            JSONParser jsonParser = new JSONParser();
            JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(getApplicationContext()) + "/" + "get_categories.php", Constants.JSON_GET, nameValuePairs);


            if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) {
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.categoriesFetchingNotSuccessful) + returnObject.optString(Constants.MESSAGE_WORD);
                return null;
            }

            JSONArray categoriesJSONArray =returnObject.optJSONArray("categories");
            if (categoriesJSONArray == null)
            {
                errorOccurred=true;
                errorMessage=getApplicationContext().getString(R.string.categoriesFetchingNotSuccessful);
                return null;
            }


            String categoryName;
            int categoryID;
            JSONObject categoryJSONObject;
            categories.clear();
            for (int i=0; i< categoriesJSONArray.length(); i++) {
                categoryJSONObject = categoriesJSONArray.optJSONObject(i);
                if (categoryJSONObject == null) {
                    errorOccurred=true;
                    errorMessage="Could not retrieve category " + i;
                    return null;
                }

                categoryName= categoryJSONObject.optString("name", "");
                categoryID= categoryJSONObject.optInt("id", -1);
                // TODO:
                if (categoryName.isEmpty() || categoryID == -1) {
                    errorOccurred = true;
                    errorMessage = "Could not retrieve category " + i;
                    return null;
                }
                categories.put(categoryName, categoryID);
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

            /*
            categoriesSpinnerAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item);
            for(String categoryName : categories.keySet())
                categoriesSpinnerAdapter.add(categoryName);

            categoriesSpinnerAdapter.notifyDataSetChanged();
            categoriesSpinner.setAdapter(categoriesSpinnerAdapter);
            */

            categoriesFilterInputField.setHint(categories.keySet().toString());

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.categoriesFetchingSuccessful), Toast.LENGTH_LONG).show();
        }
    }


}
