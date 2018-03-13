package de.htwds.mada.foodsharing;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

class JSONParser {
    private static final String LOG=JSONParser.class.getName();

    private static InputStream inputStream = null;
    private static JSONObject jObj = new JSONObject();
    private static String json = Constants.EMPTY_STRING;

    public JSONParser() {
    }

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            if (method.equals(Constants.JSON_POST)) {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpResponseEntity = httpResponse.getEntity();
                inputStream = httpResponseEntity.getContent();
            } else if (method.equals(Constants.JSON_GET)) {
                String paramString = URLEncodedUtils.format(params, Constants.JSON_UTF);
                url += Constants.QUESTIONMARK + paramString;
                HttpGet httpGet = new HttpGet(url);

                Log.i(LOG, "Request " + httpGet.getRequestLine());
                Header[] headers = httpGet.getAllHeaders();

                for (Header header : headers)
                    Log.i(LOG, "Request " + header.getName() + " " + header.getValue());

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpResponseEntity = httpResponse.getEntity();
                inputStream = httpResponseEntity.getContent();
            }
        } catch (Exception e) {
            String errorMessage=e.getLocalizedMessage();
            Log.e(LOG, Constants.HTTP_ERROR + errorMessage);
            try {
                jObj.put(Constants.SUCCESS_WORD, false);
                jObj.put(Constants.MESSAGE_WORD, errorMessage);
            } catch (JSONException ignored) { }
            return jObj;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Constants.JSON_ISO), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(Constants.NEWLINE);
            }
            reader.close();
            inputStream.close();
            json = sb.toString();
        } catch (Exception e) {
            String errorMessage=e.getLocalizedMessage();
            Log.e(LOG, Constants.CONVERTING_ERROR + errorMessage);
            try {
                jObj.put(Constants.SUCCESS_WORD, false);
                jObj.put(Constants.MESSAGE_WORD, errorMessage);
            } catch (JSONException ignored) { }
            return jObj;
        }


        try{
            jObj = new JSONObject(json);
            Log.i(LOG,Constants.LOG_SUCCESS+jObj.optInt(Constants.SUCCESS_WORD, -1)+
                    Constants.LOG_MESSAGE+jObj.optString(Constants.MESSAGE_WORD) + Constants.SPACE + json);
            if (jObj.getInt(Constants.SUCCESS_WORD) == 1) {
                jObj.put(Constants.SUCCESS_WORD, true);
            }
            else
            {
                String errorMessage=jObj.getString(Constants.MESSAGE_WORD);
                jObj.put(Constants.SUCCESS_WORD, false);
                jObj.put(Constants.MESSAGE_WORD, errorMessage);
            }
        } catch (JSONException e) {
            String errorMessage=e.getLocalizedMessage();
            Log.e(LOG, Constants.STRING_PARSING_ERROR + json + Constants.SPACE + errorMessage);
            try {
                jObj.put(Constants.SUCCESS_WORD, false);
                jObj.put(Constants.MESSAGE_WORD, errorMessage);
            } catch (JSONException ignored) { }
            return jObj;
        }

        return jObj;
    }


    public JSONObject makeMultipartHttpRequest(String url,  HttpEntity httpRequestEntity) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(httpRequestEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpResponseEntity=httpResponse.getEntity();
            inputStream = httpResponseEntity.getContent();
        } catch (Exception e) {
            String errorMessage=e.getLocalizedMessage();
            Log.e(LOG, Constants.HTTP_ERROR + errorMessage);
            try {
                jObj.put(Constants.SUCCESS_WORD, false);
                jObj.put(Constants.MESSAGE_WORD, errorMessage);
            } catch (JSONException ignored) { }
            return jObj;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Constants.JSON_ISO), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(Constants.NEWLINE);
            }
            reader.close();
            inputStream.close();
            json = sb.toString();
        } catch (Exception e) {
            String errorMessage=e.getLocalizedMessage();
            Log.e(LOG, Constants.CONVERTING_ERROR + errorMessage);
            try {
                jObj.put(Constants.SUCCESS_WORD, false);
                jObj.put(Constants.MESSAGE_WORD, errorMessage);
            } catch (JSONException ignored) { }
            return jObj;
        }

        try{
            jObj = new JSONObject(json);
            Log.i(LOG,Constants.LOG_SUCCESS+jObj.optInt(Constants.SUCCESS_WORD, -1)+
                    Constants.LOG_MESSAGE+jObj.optString(Constants.MESSAGE_WORD) + Constants.SPACE + json);
            if (jObj.getInt(Constants.SUCCESS_WORD) == 1) {
                jObj.put(Constants.SUCCESS_WORD, true);
            }
            else
            {
                String errorMessage=jObj.getString(Constants.MESSAGE_WORD);
                jObj.put(Constants.SUCCESS_WORD, false);
                jObj.put(Constants.MESSAGE_WORD, errorMessage);
            }
        } catch (JSONException e) {
            String errorMessage=e.getLocalizedMessage();
            Log.e(LOG, Constants.STRING_PARSING_ERROR + json + Constants.SPACE + errorMessage);
            try {
                jObj.put(Constants.SUCCESS_WORD, false);
                jObj.put(Constants.MESSAGE_WORD, errorMessage);
            } catch (JSONException ignored) { }
            return jObj;
        }

        return jObj;
    }
}
