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

public class JSONHandler extends AsyncTask<Void, Void, Void> {
    private final String TAG = "JSONHandler";

    private HttpURLConnection conn;
    private BufferedReader reader;
    private String TestURL = "https://jsonplaceholder.typicode.com/todos/1";


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Log.i(TAG,"get data in bg");
            getJson();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getJson() throws InterruptedException {
        try {
            URL url = new URL(TestURL);
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
                    Log.d(TAG,"json object: "+jsonObject.getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Log.e(TAG,"status code: "+statusCode);
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
    }

}
