package de.htwds.mada.foodsharing;

import android.content.Context;
import android.preference.PreferenceManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private int uid;
    private String email;
    private char[] password; //char[] because of a better security (nullable)
    /* http://stackoverflow.com/questions/10393951/getting-a-char-array-from-the-user-without-using-a-string */
    private String username;
    //address infos
    private String street;
    private String houseNumber; //String because of possible house numbering like "109 - 111a"
    private String additional;
    private int plz;  //0-99999
    private String city;
    private String country;

    //evtl. Optional
    private String vorname;
    private String nachname;

    private boolean objectHasBeenEdited=false;

    //email regexp test
    private Pattern pattern;
    private Matcher matcher;


    Context context;


    public User(String email, String password){
        setEmail(email);
        setPassword(password.toCharArray());
    }

    public User(Context context)
    {
        this.context=context;
        setID(PreferenceManager.getDefaultSharedPreferences(context).getInt(Constants.currentUserIdKey, -1));
    }

    public User(Context context, int uid)
    {
        this.context=context;
        setID(uid);
        //PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(Constants.currentUserIdKey, getID()).apply();
    }

    public void setEdited(boolean edited)
    {
        this.objectHasBeenEdited=edited;
    }

    int getID() {        return uid;    }
    void setID(int uid) {
        //test if positive
        /*
        if (uid >= 0) {
            this.uid = uid;
        } else {
            throw new NumberFormatException(Constants.NO_NEGATIVE_NUMBER);
        }
        */
        this.uid = uid;
    }

    public String getEmail() {        return email;    }
    public void setEmail(String email) {
        //test if not empty
        if (email.trim().isEmpty()) {
            throw new NullPointerException("Email address is required!");
        }
        //test if it is a correct email address
        pattern = Pattern.compile(Constants.EMAIL_REGEXP);
        matcher = pattern.matcher(email);
        if (matcher.matches()) {
            this.email = email;
        } else {
            throw new IllegalArgumentException(Constants.NO_VALID_EMAIL);
        }
    }

    char[] getPassword() {        return password;    }
    public void setPassword(char[] password) {
        //test if not empty
        if (password.length == 0 || password[0] == 0) {
            throw new NullPointerException("Password is required!");
        }
        this.password = password;
    }

    public String getUsername() {        return username;    }
    public void setUsername(String username) {
        //test if not empty
        if (username.trim().isEmpty()) {
            throw new NullPointerException("Username is required!");
        }
        this.username = username.trim();
    }

    public String getStreet() {        return street;    }
    public void setStreet(String street) {
        //test if not empty
        if (street.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.street = street.trim();
    }

    public String getHouseNumber() {        return houseNumber;    }
    public void setHouseNumber(String houseNumber) {
        //test if not empty
        if (houseNumber.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.houseNumber = houseNumber.trim();
    }

    String getAdditional() {        return additional;    }
    public void setAdditional(String additional) {
        this.additional = additional.trim();
    }

    public int getPlz() {        return plz;    }
    public void setPlz(int plz) {
        if (plz < 0 ||  plz > 99999) {
            throw new NumberFormatException(Constants.NO_VALID_PLZ);
        }
        this.plz = plz;
    }
    public void setPlz(String zipCode) {
        int plz=Integer.parseInt(zipCode);
        if (plz < 0 ||  plz > 99999) {
            throw new NumberFormatException(Constants.NO_VALID_PLZ);
        }
        this.plz = plz;
    }

    public String getCity() {        return city;    }
    public void setCity(String city) {
        if (city.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.city = city.trim();
    }

    public String getCountry() {        return country;    }
    public void setCountry(String country) {
        /*if (country.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        } else if (country.trim().length() > 2) {
            throw new IllegalArgumentException(Constants.NO_VALID_COUNTRY);
        }
        this.country = country.trim();*/
        this.country = Constants.COUNTRY_CODE_STANDARD;
    }

    public String getVorname() {        return vorname;    }
    public void setVorname(String vorname) {
        if (vorname.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.vorname = vorname.trim();
    }

    public String getNachname() {        return nachname;    }
    public void setNachname(String nachname) {
        if (nachname.trim().isEmpty()) {
            throw new NullPointerException(Constants.NO_ARGUMENT);
        }
        this.nachname = nachname.trim();
    }


    private String errorMessage;
    public String getErrorMessage() {return errorMessage; }


    public boolean fillObjectFromDatabase()
    {
        errorMessage = Constants.EMPTY_STRING;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(Constants.USER_ID_ABK, String.valueOf(this.getID())));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(this.context) + "/" + Constants.URL_GET_USER, Constants.JSON_GET, nameValuePairs);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD)) {
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD);
            return false;
        }

        JSONObject userJSONObject=returnObject.optJSONObject(Constants.NUMBER_0);
        if (userJSONObject == null) {
            errorMessage = Constants.USER_INFO_RETRIEVING_ERROR;
            return false;
        }

        this.setEmail(userJSONObject.optString(Constants.EMAIL_WORD));
        this.setVorname(userJSONObject.optString(Constants.VORNAME_WORD));
        this.setUsername(userJSONObject.optString(Constants.USERNAME_WORD));
        this.setPassword(userJSONObject.optString(Constants.PASSWORD_WORD, Constants.EMPTY_STRING).toCharArray());
        this.setNachname(userJSONObject.optString(Constants.NACHNAME_WORD));
        this.setStreet(userJSONObject.optString(Constants.STRASSE_WORD));
        this.setHouseNumber(userJSONObject.optString(Constants.HAUSNUMMER_WORD));
        this.setAdditional(userJSONObject.optString(Constants.ZUSATZ_WORD));
        this.setPlz(userJSONObject.optInt(Constants.PLZ_WORD));
        this.setCity(userJSONObject.optString(Constants.ORT_WORD));
        this.setCountry(userJSONObject.optString(Constants.LAND_WORD));

        return true;
    }

    public boolean saveObjectToDatabase()
    {
        errorMessage=Constants.EMPTY_STRING;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(Constants.EMAIL_WORD, this.getEmail()));
        nameValuePairs.add(new BasicNameValuePair(Constants.PASSWORD_WORD, String.valueOf(this.getPassword())));
        nameValuePairs.add(new BasicNameValuePair(Constants.USERNAME_WORD, this.getUsername()));
        nameValuePairs.add(new BasicNameValuePair(Constants.VORNAME_WORD, this.getVorname()));
        nameValuePairs.add(new BasicNameValuePair(Constants.NACHNAME_WORD, this.getNachname()));
        nameValuePairs.add(new BasicNameValuePair(Constants.STRASSE_WORD, this.getStreet()));
        nameValuePairs.add(new BasicNameValuePair(Constants.HAUSNUMMER_WORD, this.getHouseNumber()));
        nameValuePairs.add(new BasicNameValuePair(Constants.ZUSATZ_WORD, this.getAdditional()));
        nameValuePairs.add(new BasicNameValuePair(Constants.PLZ_WORD, String.valueOf(this.getPlz())));
        nameValuePairs.add(new BasicNameValuePair(Constants.ORT_WORD, this.getCity()));
        nameValuePairs.add(new BasicNameValuePair(Constants.LAND_WORD, this.getCountry()));

        JSONParser jsonParser = new JSONParser();
        JSONObject returnObject;
        if (this.objectHasBeenEdited) {
            nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(this.getID())));
            returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(this.context) + "/" + "update_user.php", Constants.JSON_POST, nameValuePairs);
        }
        else
            returnObject = jsonParser.makeHttpRequest(Constants.getHttpBaseUrl(this.context) + "/" + Constants.URL_CREATE_USER, Constants.JSON_POST, nameValuePairs);

        if (!returnObject.optBoolean(Constants.SUCCESS_WORD))
            errorMessage=returnObject.optString(Constants.MESSAGE_WORD, Constants.UNKNOWN_ERROR);

        return returnObject.optBoolean(Constants.SUCCESS_WORD);
    }

}
