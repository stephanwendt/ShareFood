package de.htwds.mada.foodsharing;

import android.content.Context;
import android.preference.PreferenceManager;

public class Constants {

    public static String getHttpBaseUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("preferenceHttpBaseUrl", "http://foodsharing.blankspot.de/");
    }

    public static final String keyOfferID = Constants.class.getName() + "keyOfferID";
    public static final String keyUserID = Constants.class.getName() + "keyUserID";
    public static final String currentUserIdKey = "de.htwds.mada.foodsharing.currentuserid";
    public static final String PHOTO_FILENAME = "foodSharingPhoto.jpg";

    //success messages
    public static final String ACCOUNT_UPDATED = "Account updated successfully!";
    public static final String OFFER_FETCHED = "Offer data fetched successfully!";
    public static final String OFFER_EDITED = "Offer edited successfully!";
    public static final String SIGNED_OUT = "Sign out successful!";
    public static final String USER_FETCHED = "User data fetched successfully!";
    //public static final String USER_FETCHED = "@string/userDataFetchedSuccessfully";
    public static final String WAIT_INFO = "Please wait.";
    public static final String PICTURE_WRITTEN = "Picture written successfully!";

    //missing failures
    public static final String NO_NEGATIVE_NUMBER = "Only positive numbers allowed!";
    public static final String NO_VALID_EMAIL = "No valid email address!";
    public static final String NO_VALID_PLZ = "PLZ must be between 00000 and 99999!";
    public static final String NO_VALID_COUNTRY = "The given country code is too long!";
    public static final String NOT_NEGATIVE = "No negative numbers!";
    public static final String NO_ARGUMENT = "No or empty object given!";

    //chars
    public static final String EMPTY_STRING = "";
    public static final String SPACE = " ";
    public static final String NEWLINE = "\n";
    public static final String QUESTIONMARK = "?";
    public static final String AT_SIGN = "@";

    public static final String EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //abkuerzings
    public static final String ID_ABK = "id";
    public static final String OFFER_ID_ABK = "oid";
    public static final String USER_ID_ABK = "uid";
    public static final String DESCRIPTION_ABK = "descr";
    public static final String COUNTRY_CODE_STANDARD = "DE";

    //URIs
    public static final String HTTP_BASE_URL = "http://foodsharing.blankspot.de/";
    public static final String URL_CREATE_OFFER = "create_offer.php";
    public static final String URL_GET_OFFER = "get_offer_details.php";
    public static final String URL_CREATE_USER = "create_user.php";
    public static final String URL_GET_USER = "get_user_details.php";
    public static final String URL_GET_ALL_OFFERS = "get_all_offers.php";
    public static final String URL_GET_CATEGORIES = "get_categories.php";
    public static final String URL_GET_ATTRIBUTES ="get_attributes.php";
    public static final String URL_CREATE_OFFER_WITH_IMAGE = "create_offer_with_image.php";
    public static final String URL_GET_ATTRIBUTE_BY_ID = "get_attribute_by_id.php?aid=";
    public static final String URL_GET_CATEGORY_BY_ID ="get_category_by_id.php?cid=";
    public static final String URL_GET_ATTRIBUTE_IDS_FROM_OFFER = "get_offer_attributes.php?oid=";
    public static final String URL_GET_CATEGORY_IDS_FROM_OFFER = "get_offer_categories.php?oid=";
    public static final String URL_GET_USERID_WITH_EMAIL_AND_PASSWORD = "get_user_with_email_and_password.php";

    //JSON stuff
    public static final String JSON_POST = "POST";
    public static final String JSON_GET = "GET";
    public static final String JSON_UTF = "utf-8";
    public static final String JSON_ISO = "iso-8859-1";
    public static final String JSON_TRANS_ID = "transaction_id";
    public static final String JSON_IMAGE_ID = "image_id";
    public static final String JSON_VALID_DATE = "valid_date";

    //single words
    public static final String SUCCESS_WORD = "success";
    public static final String MESSAGE_WORD = "message";
    public static final String OFFER_WORD = "offer";
    public static final String OFFERS_WORD = "offers";
    public static final String TITLE_WORD= "title";
    public static final String DATE_WORD = "date";
    public static final String DATA_WORD = "data";
    public static final String BLA_WORD = "bla";
    public static final String SELECTED_WORD = "selected";
    public static final String CATEGORY_WORD = "Category";

    //address words
    public static final String EMAIL_WORD = "email";
    public static final String VORNAME_WORD = "vorname";
    public static final String USERNAME_WORD = "username";
    public static final String NACHNAME_WORD = "nachname";
    public static final String STRASSE_WORD = "strasse";
    public static final String HAUSNUMMER_WORD = "hausnummer";
    public static final String ZUSATZ_WORD = "zusatz";
    public static final String PLZ_WORD = "plz";
    public static final String ORT_WORD = "ort";
    public static final String LAND_WORD = "land";
    public static final String PASSWORD_WORD = "password";
    public static final String DIALOG_WORD = "dialog";
    public static final String NEW_PROFILE = "newProfile";
    public static final String RESULTS_FILTERED_BY_USER = "resultsFilteredByUser";


    //multiple words
    public static final String LOG_SUCCESS = "Success: ";
    public static final String LOG_MESSAGE = ", message: ";
    public static final String CREATE_OFFER = "Create Offer";
    public static final String EDIT_OFFER =  "Edit Offer";
    public static final String OFFER_ID = "Offer id: ";
    public static final String BEST_BEFORE = "Best before ";
    public static final String CATEGORY = "Category: ";
    public static final String REGISTER_ACCOUNT = "Do you want to register an account with us?";
    public static final String USER_ID = "user-id";
    public static final String PLEASE_WAIT = "Please wait";
    public static final String USER_ID_MESSAGE = "User ID  ";
    public static final String IN_ONACTIVITY_RESULT = "In onActivityResult ";
    public static final String RESULT_OK = ": result ok";

    //Numbers
    public static final String NUMBER_0 = "0";
    public static final int EXAMPLE_PLZ = 12345;

    //Errors
    public static final String HTTP_ERROR = "Error in http connection ";
    public static final String CONVERTING_ERROR = "Error converting result ";
    public static final String STRING_PARSING_ERROR = "Error parsing result string ";
    public static final String OFFER_INFO_RETRIEVING_ERROR = "Could not retrieve offer info!";
    public static final String USER_INFO_RETRIEVING_ERROR = "Could not retrieve user info!";
    public static final String UNKNOWN_ERROR = "Unknown error!";
    public static final String LOGIN_INCORRECT = "Login incorrect!";
    public static final String IMAGE_TO_FILE_ERROR = "Error writing image to file!";

    public static final String[] CATEGORIES_ARRAY = {"Fleisch", "Fisch", "Obst", "Gemüse", "Backwaren"};

    //tags
    public static final String DATEPICKER_TAG = "datePicker";
    public static final String CATEGORY_DIALOG_TAG = "categoryDialog";



}
