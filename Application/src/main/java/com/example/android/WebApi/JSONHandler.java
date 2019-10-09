package com.example.android.WebApi;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JSONHandler extends AsyncTask<Void, Void, String> {
    private final String TAG = "JSONHandler";
    private String comeString;
    private HttpURLConnection conn;
    private BufferedReader reader;
    private final String TestURL = "https://jsonplaceholder.typicode.com/todos/";

    private final String PTXURL = "https://ptx.transportdata.tw/MOTC/v2/Air/FIDS/Airport/TPE?$top=1&$format=JSON";

    final String APP_ID = "18751db3f6c04cd19fa069f23600f5e9";
    final String APP_Key = "SuiPq2tko8qqjt623IJCmMZLM2s";

    //取得加密簽章


    public JSONHandler(String comeString) throws SignatureException {
        this.comeString = comeString;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String jsonResult = null;
        Log.d(TAG,"comeString: "+comeString);
//        try {
            Log.i(TAG, "get data in bg");
//            if (comeString.startsWith("")) {
//                Log.d(TAG,"FUCK");
//                getPTXJson();
//            }else {
                jsonResult = getTestJson();
//            }
//        } catch (InterruptedException | SignatureException e) {
//            e.printStackTrace();
//        }
        return jsonResult;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
    }

    private void getPTXJson() throws InterruptedException, SignatureException {
        String jsonString = null;
        String xdate = getServerTime();
        String SignDate = "x-date: " + xdate;
        String Signature = Signature(SignDate, APP_Key);
        String sAuth = "hmac username=\"" + APP_ID + "\", algorithm=\"hmac-sha1\", headers=\"x-date\", signature=\"" + Signature + "\"";

        try {
            URL url = new URL(PTXURL + comeString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Authorization", sAuth);
            conn.setRequestProperty("x-date", xdate);
//            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setDoInput(true);
            conn.connect();
            int statusCode = conn.getResponseCode();
            Log.d(TAG, "status code: " + statusCode);

            if (statusCode == 200) {
                InputStream is = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(TAG, "resp: " + sb.toString());
                try {
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    Log.d(TAG, "json object: " + jsonObject.getString("title"));
                    jsonString = jsonObject.getString("AirportID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.w(TAG, "get json error --> status code: " + statusCode);
                jsonString = String.valueOf("HTTP: " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        return jsonString;
    }


    private String getTestJson() {
        String jsonString = null;
        try {
            URL url = new URL(TestURL + comeString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setDoInput(true);
            conn.connect();
            int statusCode = conn.getResponseCode();
            Log.d(TAG, "status code: " + statusCode);

            if (statusCode == 200) {
                InputStream is = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(TAG, "resp: " + sb.toString());
                try {
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    Log.d(TAG, "json object: " + jsonObject.getString("title"));
                    jsonString = jsonObject.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.w(TAG, "get json error --> status code: " + statusCode);
                jsonString = String.valueOf("HTTP:" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonString;
    }

    private static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    private static String Signature(String xData, String AppKey) throws java.security.SignatureException {
        try {
            Base64.Encoder encoder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                encoder = Base64.getEncoder();
            }
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(AppKey.getBytes("UTF-8"), "HmacSHA1");

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(xData.getBytes("UTF-8"));
            String result = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                result = encoder.encodeToString(rawHmac);
            }
            return result;

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
    }

}
