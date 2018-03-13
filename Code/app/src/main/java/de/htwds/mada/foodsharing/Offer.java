package de.htwds.mada.foodsharing;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Offer {
    private static final String LOG=Offer.class.getName();

    private int offerID;
    private int offererID=-1;
    private int transactID;
    private int category;
    private String shortDescription;
    private String longDescription;
    private File picture;
    private int pictureID=-1;

    private Calendar dateAdded;
    private String pickupTimes; //too complex to use date or time types
    private final Calendar mhd = new GregorianCalendar();


    private Context context;

    private boolean objectHasBeenEdited=false;
    private boolean pictureHasBeenEdited=false;


    private HashMap<String,Integer> categories= new HashMap<String,Integer>();

    //Exceptions

    public Offer(Context context) {
        this.context=context;
    }

    public Offer(Context context, JSONObject offerJSONObject)
    {
        this.fillObjectFromJSONObject(offerJSONObject);

        this.context=context;
    }

    public void setEdited(boolean edited)
    {
        this.objectHasBeenEdited=edited;
    }

    public int getID() {        return offerID;    }
    public void setID(int offerID) {
        /*
        if (offerID < 0) {
            throw new NumberFormatException(NOT_NEGATIVE);
        }
        */
        this.offerID = offerID;
    }

    public int getOffererID() throws Exception
    {
        if (offererID > 0) return offererID;

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("tid", String.valueOf(this.getTransactID())));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(context) + "/" + "get_transaction_details.php", Constants.JSON_GET, nameValuePairs);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
        {
            throw new Exception("Could not retrieve offerer ID!");

        }

        JSONArray transactionJSONArray=returnObject.optJSONArray("transaction");
        JSONObject transactionJSONObject=transactionJSONArray.optJSONObject(0);
        if (transactionJSONObject == null)
        {
            throw new Exception("Could not retrieve offerer ID!");
        }

        offererID=transactionJSONObject.optInt("offerer_id", -1);

        return offererID;
    }

    public int getTransactID() {        return transactID;    }
    public void setTransactID(int transactID) {
        if (transactID < 0) {
            throw new NumberFormatException(Constants.NOT_NEGATIVE);
        }
        this.transactID = transactID;
    }

    public int getCategory() {        return category;    }
    public void setCategory(int category) {
        if (category < 0) {
            throw new NumberFormatException(Constants.NOT_NEGATIVE);
        }
        this.category = category;
    }

    public HashMap<String,Integer> getCategories()
    {
        return categories;
    }
    public void setCategories(HashMap<String,Integer> categories) {
        this.categories=categories;
    }

    public void setCategories(String categoriesString) {
        if (categoriesString == null
                || categoriesString.trim().isEmpty())
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);

        categories.clear();

        //TODO: das muss in der activity geschehen, und als Argument wird nur eine HashMap übergeben
        String[] categoryStrings=categoriesString.split(",");
        for (String category: categoryStrings)
        {
           if (category.trim().isEmpty())
               continue;

            categories.put(category, 1);
        }
    }

    public String getShortDescription() {        return shortDescription;    }
    public void setShortDescription(String shortDescription) {
        if (shortDescription.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);
        }
        this.shortDescription = shortDescription.trim();
    }

    public String getLongDescription() {        return longDescription;    }
    public void setLongDescription(String longDescription) {
        if (longDescription.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);
        }
        this.longDescription = longDescription.trim();
    }

    public File getPicture()
    {
        if (this.picture == null)
            this.retreivePictureFromDatabase(this.pictureID);
        if (this.picture == null
                || ! this.picture.isFile()
                || ! this.picture.canRead())
            return null;

        return this.picture;
    }

    public void setPicture(File picture) {
        if (picture == null
                || !picture.isFile()
                || !picture.canRead())
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);

        this.picture = picture;
    }

    public void setPicture(byte[] picture)
    {
        File photoFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.PHOTO_FILENAME);

        try ( FileOutputStream fileOutputStream = new FileOutputStream(photoFile)) {
            fileOutputStream.write(picture);
            fileOutputStream.close();
        } catch (Exception ex) {
            Log.e(LOG, Constants.IMAGE_TO_FILE_ERROR);
            if (photoFile != null) photoFile.delete();
            this.picture=null;
            return;
        }

        Log.i(LOG, Constants.PICTURE_WRITTEN);
        this.setPicture(photoFile);
    }

    public void setPicture(Bitmap picture)
    {
        if (picture == null)
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);

        File photoFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.PHOTO_FILENAME);

        try ( FileOutputStream fileOutputStream = new FileOutputStream(photoFile)) {
            picture.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception ex) {
            if (photoFile != null) photoFile.delete();
            this.picture=null;
            Log.e(LOG, Constants.IMAGE_TO_FILE_ERROR);
            return;
        }

        Log.i(LOG, Constants.PICTURE_WRITTEN);
        this.setPicture(photoFile);
    }

    public void setPictureEdited(boolean edited)
    {
        this.pictureHasBeenEdited=edited;
    }

    public Calendar getMhd() {        return mhd;    }
    public void setMhd(int year, int month, int day) {
        mhd.setLenient(false);          //make calendar validating
        mhd.set(year, month, day); //throws exception if date is invalid
    }
    public void setMhd(long secondsSinceEpoch)
    {
        mhd.setTimeInMillis(secondsSinceEpoch*1000);
    }
    public void setMhd(String bbdString) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            mhd.setTime(simpleDateFormat.parse(bbdString));
            Log.i(LOG, String.format("%tF", this.mhd));
        } catch (Exception ex)
        {
            mhd.setTimeInMillis(0);
            Log.e(LOG, String.format("%tF", this.mhd));
        }
    }

    public Calendar getDateAdded()
    {
        if (dateAdded == null)
            dateAdded=Calendar.getInstance();
        return dateAdded;
    }
    private void setDateAdded(String dateAddedString) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (dateAdded == null)
            dateAdded=Calendar.getInstance();

        try {
            this.dateAdded.setTime(simpleDateFormat.parse(dateAddedString));
        } catch (Exception ex) { this.dateAdded.setTimeInMillis(0); }
    }

    public String getPickupTimes() {        return pickupTimes;    }
    public void setPickupTimes(String pickupTimes) {
        if (pickupTimes.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);
        }
        this.pickupTimes = pickupTimes.trim();
    }

    private String errorMessage;
    public String getErrorMessage() {return errorMessage; }


    public boolean fillObjectFromDatabase() {
        errorMessage = Constants.EMPTY_STRING;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(Constants.OFFER_ID_ABK, String.valueOf(this.getID())));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(context) + "/" + Constants.URL_GET_OFFER, Constants.JSON_GET, nameValuePairs);


        if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) {
            errorMessage = returnObject.optString(Constants.MESSAGE_WORD);
            return false;
        }

        JSONArray offerJSONArray=returnObject.optJSONArray(Constants.OFFER_WORD);
        JSONObject offerJSONObject=offerJSONArray.optJSONObject(0);

        if (offerJSONObject == null)
        {
            errorMessage=Constants.OFFER_INFO_RETRIEVING_ERROR;
            return false;
        }

        this.fillObjectFromJSONObject(offerJSONObject);

        this.getCategoriesFromDatabase();


        return true;
    }

    private void fillObjectFromJSONObject(JSONObject offerJSONObject)    {
        this.setID(offerJSONObject.optInt(Constants.ID_ABK, -1));
        this.setTransactID(offerJSONObject.optInt(Constants.JSON_TRANS_ID, -1));
        this.setShortDescription(offerJSONObject.optString(Constants.TITLE_WORD));
        this.setLongDescription(offerJSONObject.optString(Constants.DESCRIPTION_ABK));
        this.setMhd( offerJSONObject.optLong("bbd", -1) );
        this.setDateAdded(offerJSONObject.optString("date", ""));
        //TODO: this.setValidDate(userJSONObject.optLong("valid_date"));
        Log.i(LOG, "filling");
        //TODO: handle errors:
        this.pictureID=offerJSONObject.optInt("image_id", -1);
    }

    private boolean retreivePictureFromDatabase(int imageID)    {
        errorMessage = Constants.EMPTY_STRING;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("pid", String.valueOf(imageID)));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(context) + "/" + "get_image.php", Constants.JSON_GET, nameValuePairs);

        Log.i(LOG, "Getting image with id " + imageID);
        if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) {
            errorMessage = returnObject.optString(Constants.MESSAGE_WORD);
            return false;
        }

        JSONArray imageJSONArray=returnObject.optJSONArray("image");
        JSONObject imageJSONObject=imageJSONArray.optJSONObject(0);

        if (imageJSONObject == null) {
            errorMessage = "Failed to fetch image!";
            return false;
        }

        try {
            this.setPicture(Base64.decode(imageJSONObject.optString("image", ""), Base64.DEFAULT));
        } catch (Exception ex) {
            Log.e(LOG, "Error decoding image!");
            errorMessage="Error decoding image!";
            return false;
        }

        return true;
    }

    private boolean getCategoriesFromDatabase() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(Constants.OFFER_ID_ABK, String.valueOf(this.getID())));


        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(context) + "/" + "get_offer_categories.php", Constants.JSON_GET, nameValuePairs);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) {
            errorMessage=this.context.getString(R.string.categoriesFetchingNotSuccessful) + returnObject.optString(Constants.MESSAGE_WORD);
            return false;
        }

        JSONArray categoriesJSONArray =returnObject.optJSONArray("categories");
        if (categoriesJSONArray == null)
        {
            errorMessage=this.context.getString(R.string.categoriesFetchingNotSuccessful);
            return false;
        }

        String categoryName;
        int categoryID;
        JSONObject categoryJSONObject;
        categories.clear();
        for (int i=0; i< categoriesJSONArray.length(); i++) {
            categoryJSONObject = categoriesJSONArray.optJSONObject(i);
            if (categoryJSONObject == null) {
                errorMessage="Could not retrieve category " + i;
                return false;
            }

            categoryID= categoryJSONObject.optInt("cat_id", -1);
            // TODO:
            if (categoryID == -1) {
                errorMessage = "Could not retrieve category " + i;
                return false;
            }
            nameValuePairs.clear();
            nameValuePairs.add(new BasicNameValuePair("cid", String.valueOf(categoryID)));
            returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(context) + "/" + "get_category_by_id.php", Constants.JSON_GET, nameValuePairs);

            if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) {
                errorMessage=this.context.getString(R.string.categoriesFetchingNotSuccessful) + returnObject.optString(Constants.MESSAGE_WORD);
                return false;
            }

            JSONArray categoryJSONArray =returnObject.optJSONArray("category");
            if (categoryJSONArray == null)
            {
                errorMessage=this.context.getString(R.string.categoriesFetchingNotSuccessful);
                return false;
            }

            JSONObject catJSONObject=categoryJSONArray.optJSONObject(0);
            if (catJSONObject == null)
            {
                errorMessage=this.context.getString(R.string.categoriesFetchingNotSuccessful);
                return false;
            }
            categoryName= catJSONObject.optString("name", "");
            //categoryID= catJSONObject.optInt("id", -1);
            // TODO:
            if (categoryName.isEmpty()) {
                errorMessage=this.context.getString(R.string.categoriesFetchingNotSuccessful);
                return false;
            }
            categories.put(categoryName, categoryID);
        }

        return true;
    }






    /*
    public boolean saveObjectToDatabase()
    {
        errorMessage= Constants.EMPTY_STRING;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_TRANS_ID,String.valueOf(this.getTransactID())));
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_IMAGE_ID,"4"));
        nameValuePairs.add(new BasicNameValuePair(Constants.TITLE_WORD, this.getShortDescription()));
        nameValuePairs.add(new BasicNameValuePair(Constants.DESCRIPTION_ABK, this.getLongDescription()));
        nameValuePairs.add(new BasicNameValuePair("bbd", this.getMhd().toString()));
        Timestamp timestamp=new Timestamp(this.getDateAdded().getTimeInMillis());
        nameValuePairs.add(new BasicNameValuePair(Constants.DATE_WORD, timestamp.toString()));
        nameValuePairs.add(new BasicNameValuePair(Constants.JSON_VALID_DATE, "1423216493"));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(getApplicationContext()) + "/" + Constants.URL_CREATE_OFFER, Constants.JSON_POST, nameValuePairs);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD, Constants.UNKNOWN_ERROR);

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }
    */


    public boolean saveObjectToDatabase()    {
        /* http://stackoverflow.com/questions/16293388/how-to-send-the-string-array-of-values-in-one-key-word-using-post-method-to-the */
        errorMessage= Constants.EMPTY_STRING;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        //TODO: How do we detect only updated attributes?
        if (!this.objectHasBeenEdited
                || (this.objectHasBeenEdited && this.pictureHasBeenEdited)) {
            File pictureFile = this.getPicture();
            if (pictureFile != null) {
                builder.addPart("image", new FileBody(pictureFile));
                Log.i(LOG, "Picture was set!");
            }
        }
        builder.addTextBody("bbd", String.valueOf(this.getMhd().getTimeInMillis()/1000));
        builder.addTextBody(Constants.TITLE_WORD, this.getShortDescription());
        builder.addTextBody(Constants.DESCRIPTION_ABK, this.getLongDescription());
        builder.addTextBody(Constants.JSON_VALID_DATE, "1423216493");
        builder.addTextBody("offerer_id", String.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getInt(Constants.currentUserIdKey, -1)));

        for (int categoryID : categories.values()) {
            Log.i(LOG, "Category: " + categoryID);
            try {
                builder.addPart("categories[]", new StringBody(String.valueOf(categoryID)));
            } catch (Exception e) {
            }
        }


        if (this.objectHasBeenEdited) {
            builder.addTextBody("id", String.valueOf(this.getID()));
        }
        HttpEntity httpRequestEntity = builder.build();

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject;
        if (this.objectHasBeenEdited) {
            returnObject = jsonParser.makeMultipartHttpRequest(Constants.getHttpBaseUrl(context) + "/" + "update_offer_with_image.php", httpRequestEntity);
        }
        else
            returnObject = jsonParser.makeMultipartHttpRequest(Constants.getHttpBaseUrl(context) + "/" + "create_offer_with_image_and_transaction.php", httpRequestEntity);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD, Constants.UNKNOWN_ERROR);


        //TODO: setimageid

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }


    /*    Currently only for the items in ListView     */
    public String toString()    {
        return getShortDescription() + Constants.NEWLINE + getLongDescription();
    }
}
