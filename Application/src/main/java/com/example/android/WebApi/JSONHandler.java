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

public class JSONHandler extends AsyncTask<Void, Void, String> {
    private final String TAG = "JSONHandler";
    private String comeString;
    private HttpURLConnection conn;
    private BufferedReader reader;
    private final String TestURL = "https://jsonplaceholder.typicode.com/todos/";

    public JSONHandler(String comeString) {
        this.comeString = comeString;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String jsonResult = null;
        try {
            Log.i(TAG, "get data in bg");
            jsonResult = getJson();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
    }

    private String getJson() throws InterruptedException {
        String jsonString = null;
        try {
            URL url = new URL(TestURL + comeString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
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
                jsonString = String.valueOf("HTTP:"+statusCode);
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

}
