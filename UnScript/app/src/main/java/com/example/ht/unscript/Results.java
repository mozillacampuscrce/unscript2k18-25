package com.example.ht.unscript;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Results extends AppCompatActivity implements View.OnClickListener{

    Button ut1,ut2,ut3,frslt;
    OkHttpClient client = new OkHttpClient();
    JSONObject postdata;
    RequestBody body;
    Request request;
    Response responseapi;
    SharedPreferences sharedpreferences;
    public static final String pref = "result" ;
    String DataPayload = "";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    String bda,snmr,cc,ds,result,percentage;

    private static Button downloadPdf, downloadDoc, downloadZip, downloadVideo, downloadMp3, openDownloadedFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        initViews();
        setListeners();

        ut1=findViewById(R.id.ut1);
        ut2=findViewById(R.id.ut2);
        ut3=findViewById(R.id.ut3);
        frslt=findViewById(R.id.finalrs);
        client = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .build();

        ut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result="ut1";
                callapiut1();


            }
        });

        ut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result="ut2";
                callapiut1();
            }
        });
        frslt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result="sem";
                callapiut1();
            }
        });

    }

    public void callapiut1()
    {
        final String url = "http://40.90.191.188:8009/marksDisplay";
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                postdata=new JSONObject();

                try {
                    postdata.put("rollNo","101");
                    postdata.put("result",result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();
                Log.i("res",result);
                ApiSucess(result);
                //tv.setText(result);
            }

        }.execute();
    }
    public void  ApiSucess(String result)
    {
        try {

            JSONObject jsonObject1 = new JSONObject(result);
            bda = jsonObject1.optString("bda");
            cc = jsonObject1.optString("cc");
            snmr = jsonObject1.optString("snmr");
            ds = jsonObject1.optString("ds");
            percentage=jsonObject1.optString("percentage");

            Intent i= new Intent(getApplicationContext(),resultview.class);
            Bundle b=new Bundle();
            b.putString("bda",bda);
            b.putString("cc",cc);
            b.putString("snmr",snmr);
            b.putString("ds",ds);
            b.putString("percentage",percentage);

            Toast.makeText(getBaseContext(),bda,Toast.LENGTH_LONG).show();
            i.putExtras(b);
            startActivity(i);

//                Double rating = Double.valueOf(hrating);
//                rating = rating/2;
//
//                hrating = String.valueOf(rating);
//                Log.i("rat",hrating);

            //list.add(new values(hrating,hlat,hlong,hname));
//                list.add(new values(hlat, hlong, hname, hrating,desc,hurl));
//                recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                recyclerView.setAdapter(new ValuesAdapter(this, list));
            Log.i("snmr",snmr);

            Log.i("res1", String.valueOf(jsonObject1));
            //hotelTitle.setText(hname);

            // sever.setText((CharSequence) sever);



            //int i;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //set(hrating,hlat,hlong,hname);

    }

    private void setListeners() {
        downloadPdf.setOnClickListener(this);
        downloadDoc.setOnClickListener(this);
        downloadZip.setOnClickListener(this);
        downloadVideo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //Before starting any download check internet connection availability
        switch (view.getId()) {
            case R.id.downloadPdf:
                if (isConnectingToInternet())
                    new DownloadTask(this, downloadPdf, Utils.downloadPdfUrl);
                else
                    Toast.makeText(this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.downloadDoc:
                if (isConnectingToInternet())
                    new DownloadTask(this, downloadDoc, Utils.downloadDocUrl);
                else
                    Toast.makeText(this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.downloadZip:
                if (isConnectingToInternet())
                    new DownloadTask(this, downloadZip, Utils.downloadZipUrl);
                else
                    Toast.makeText(this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.downloadVideo:
                if (isConnectingToInternet())
                    new DownloadTask(this, downloadZip, Utils.downloadVideoUrl);
                else
                    Toast.makeText(this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                break;

        }

    }
    private void openDownloadedFolder() {
        //First check if SD Card is present or not
        if (new CheckForSDCard().isSDCardPresent()) {

            //Get Download Directory File
            File apkStorage = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + Utils.downloadDirectory);

            //If file is not present then display Toast
            if (!apkStorage.exists())
                Toast.makeText(this, "Right now there is no directory. Please download some file first.", Toast.LENGTH_SHORT).show();

            else {

                //If directory is present Open Folder

                /** Note: Directory will open only if there is a app to open directory like File Manager, etc.  **/

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                        + "/" + Utils.downloadDirectory);
                intent.setDataAndType(uri, "*/*");
                startActivity(Intent.createChooser(intent, "Open Download Folder"));
            }

        } else
            Toast.makeText(this, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void initViews() {
        downloadPdf = (Button) findViewById(R.id.downloadPdf);
        downloadDoc = (Button) findViewById(R.id.downloadDoc);
        downloadZip = (Button) findViewById(R.id.downloadZip);
        downloadVideo=(Button)findViewById(R.id.downloadVideo);
    }
}
