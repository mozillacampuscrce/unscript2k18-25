package com.example.ht.unscript;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profile extends AppCompatActivity {

    // api
    static OkHttpClient client = new OkHttpClient();
    static JSONObject postdata, resultData;
    static RequestBody body;
    static Request request;
    static Response responseapi;
    static String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    String rollNo = null;

    TextView user_profile_name, stdrl, stdbd, stddep, stdmob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_profile_name = findViewById(R.id.user_profile_name);
        stdrl = findViewById(R.id.stdrl);
        stdbd = findViewById(R.id.stdbd);
        stddep = findViewById(R.id.stddep);
        stdmob = findViewById(R.id.stdmob);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        rollNo = sharedpreferences.getString("rollNo", null);

        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        callApi();
    }

    private void callApi() {

        final String url = getString(R.string.urlprofile);

        if (rollNo == null)
            rollNo = "101";

        Log.d("----", url);
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                postdata = new JSONObject();
                try {
                    postdata.put("rollNo", rollNo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.d("------------", postdata.toString());

                body = RequestBody.create(MEDIA_TYPE, postdata.toString());

                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                responseapi = null;
                try {
                    responseapi = client.newCall(request).execute();
                    DataPayload = responseapi.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return DataPayload;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                ApiSucess(result);
            }

        }.execute();
    }

    private void ApiSucess(String result) {
        try {
            resultData = new JSONObject(result);

            String dob, date, month, year;
            dob = resultData.getString("dob");
            date = dob.substring(0, 2);
            month = dob.substring(2, 4);
            year = dob.substring(4, 8);
            dob = date + "-" + month + "-" + year;

            // user_profile_name, stdrl, stdbd, stddep, stdmob;
            user_profile_name.setText(resultData.getString("name"));
            stdrl.setText(rollNo);
            stdbd.setText(dob);
            stddep.setText(resultData.getString("disc"));
            stdmob.setText(resultData.getString("no"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
