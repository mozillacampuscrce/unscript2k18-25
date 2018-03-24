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
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Attendance extends AppCompatActivity {

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

    String snmr, ds, bda, cc, total;
    int grandTotal, lectTotal;
    float overAllPrec, sub1Prec, sub2Prec, sub3Prec, sub4Prec;
    String gT,lT, oAP, s1P, s2P, s3P, s4P;

    TextView sub1lec, sub2lec, sub3lec, sub4lec;
    TextView totlec;
    TextView total1, total2, total3, total4;
    TextView ftotal;
    TextView totpercentage;
    TextView sub1per, sub2per, sub3per, sub4per;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        sub1lec = findViewById(R.id.sub1lec);
        sub2lec = findViewById(R.id.sub2lec);
        sub3lec = findViewById(R.id.sub3lec);
        sub4lec = findViewById(R.id.sub4lec);
        totlec = findViewById(R.id.totlec);
        total1 = findViewById(R.id.total1);
        total2 = findViewById(R.id.total2);
        total3 = findViewById(R.id.total3);
        total4 = findViewById(R.id.total4);
        ftotal = findViewById(R.id.ftotal);
        totpercentage = findViewById(R.id.totpercentage);
        sub1per = findViewById(R.id.sub1per);
        sub2per = findViewById(R.id.sub2per);
        sub3per = findViewById(R.id.sub3per);
        sub4per = findViewById(R.id.sub4per);

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

        final String url = getString(R.string.urlAttendance);

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

                //Log.d("----", result);
                //Toast.makeText(getBaseContext(),"Res : " + result,Toast.LENGTH_SHORT).show();
                /*
                styleableToast1 = new StyleableToast
                        .Builder(CameraActv.this)
                        .duration(Toast.LENGTH_SHORT)
                        .text("Res : " + result)
                        .textColor(Color.parseColor("#FFDA44"))
                        .textBold()
                        .cornerRadius(5)
                        .build();
                styleableToast1.show();
                */

                ApiSucess(result);
            }

        }.execute();
    }

    private void ApiSucess(String result) {
        try {
            resultData = new JSONObject(result);

            snmr = resultData.getString("snmr");
            cc = resultData.getString("cc");
            ds = resultData.getString("ds");
            bda = resultData.getString("bda");
            total = resultData.getString("total");

            lectTotal = Integer.parseInt(snmr) + Integer.parseInt(cc) + Integer.parseInt(ds) + Integer.parseInt(bda);
            grandTotal = Integer.parseInt(total) * 4;
            overAllPrec = ((float) lectTotal / (float) grandTotal) * 100;
            sub1Prec = ((float) Integer.parseInt(snmr) / (float) Integer.parseInt(total)) * 100;
            sub2Prec = ((float) Integer.parseInt(cc) / (float) Integer.parseInt(total)) * 100;
            sub3Prec = ((float) Integer.parseInt(ds) / (float) Integer.parseInt(total)) * 100;
            sub4Prec = ((float) Integer.parseInt(bda) / (float) Integer.parseInt(total)) * 100;

            lT = String.valueOf(lectTotal);
            gT = String.valueOf(grandTotal);
            oAP = String.valueOf(overAllPrec);
            oAP = oAP.substring(0, 6);
            s1P = String.valueOf(sub1Prec);
            s2P = String.valueOf(sub2Prec);
            s3P = String.valueOf(sub3Prec);
            s4P = String.valueOf(sub4Prec);
            s1P = s1P.substring(0, 6);
            s2P = s2P.substring(0, 6);
            s3P = s3P.substring(0, 6);
            s4P = s4P.substring(0, 6);

            sub1lec.setText(snmr);
            sub2lec.setText(cc);
            sub3lec.setText(ds);
            sub4lec.setText(bda);
            totlec.setText(lT);
            total1.setText(total);
            total2.setText(total);
            total3.setText(total);
            total4.setText(total);
            ftotal.setText(gT);
            totpercentage.setText(oAP);
            sub1per.setText(s1P);
            sub2per.setText(s2P);
            sub3per.setText(s3P);
            sub4per.setText(s4P);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
