package com.example.android.WebApi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONHandler extends AsyncTask<String, Void, String> {
    private String TAG = "JSONHandler";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        //改寫這邊,呼叫方法時可以傳進多個String,所以這邊改抓params

        String UrlLocation = "https://ptx.transportdata.tw/MOTC/v2/Bus/Route/City/"; //API位置
        String PostData = "Taipei/706?$top=1&$format=JSON"; //要傳資料

        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL Url = new URL(UrlLocation);
            conn = (HttpURLConnection) Url.openConnection();
            conn.setRequestMethod("POST"); //要呼意的方式 Get Or Post
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            //開始傳輸資料過去給API
            OutputStream Output = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Output, StandardCharsets.UTF_8));
            writer.write(PostData);
            writer.flush();
            writer.close();
            Output.close();

            //讀取API回傳的值
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ex) {
            Log.e("API_Post", ex.getMessage());
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return sb.toString();
    }

    @Override
    protected void onPostExecute(String Result) {
        Log.d(TAG,"json result: "+Result);
        super.onPostExecute(Result);
    }
}
