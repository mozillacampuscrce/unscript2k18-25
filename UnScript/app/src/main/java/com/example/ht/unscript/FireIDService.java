package com.example.ht.unscript;

/**
 * Created by CGT on 09-11-2017.
 */
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FireIDService extends FirebaseInstanceIdService {

    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    /*
    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        Log.d("Not","Token ["+tkn+"]");

    }
    */

    public FireIDService() {
        super();
    }

    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        sharedpreferences = getApplicationContext().getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("tokenFirebase", recent_token);
        editor.commit();
    }
}