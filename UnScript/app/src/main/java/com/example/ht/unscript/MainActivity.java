package com.example.ht.unscript;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    String tokenFirebase, rollNo, password;
    String resName, resJabber;

    EditText rollNoEt, passwordEt;
    Button login;

    // api
    static OkHttpClient client = new OkHttpClient();
    static JSONObject postdata, resultData;
    static RequestBody body;
    static Request request;
    static Response responseapi;
    static String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        rollNoEt = findViewById(R.id.rollNo);
        passwordEt = findViewById(R.id.password);
        login = findViewById(R.id.login);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        tokenFirebase = sharedpreferences.getString("tokenFirebase", null);
        Log.d("----", tokenFirebase);

        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        if (!sharedpreferences.getBoolean("logincheck", false)) {
            //Toast.makeText(getBaseContext(),"signed",Toast.LENGTH_SHORT).show();
        } else {
            // not signed in. Show the "sign in" button and explanation.
            // ...
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
            finish();
        }

        login.setOnClickListener(this);
        //startActivity(new Intent(this, DashBoard.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                //startActivity(new Intent(this, DashBoard.class));
                checkLogin();
                break;
        }
    }

    private void checkLogin(){

        rollNo = rollNoEt.getText().toString();
        password = passwordEt.getText().toString();

        final String url = getString(R.string.urllogin);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                postdata = new JSONObject();
                try {
                    postdata.put("rollNo", rollNo);
                    postdata.put("password", password);
                    postdata.put("tokenFirebase", tokenFirebase);
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

        Log.d("----", result);
        try {
            resultData = new JSONObject(result);

            resName = resultData.getString("name");

            if (resName == null)
                resName = "wrong";

            if (resName.equals("wrong")) {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                passwordEt.setText("");
            }
            else {
                resJabber = resultData.getString("email");
                final String[] jaber = resJabber.split("\\@");
                resJabber = jaber[0];

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("rollNo", rollNo);
                editor.putString("jabberId", resJabber);
                editor.putBoolean("logincheck", true);
                editor.apply();

                startActivity(new Intent(this, DashBoard.class));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
