package com.example.ht.unscript;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    CardView profile, chat, attendannce, registercomplaint, results, watsoncard;

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        profile = findViewById(R.id.profile);
        chat = findViewById(R.id.chat);
        attendannce = findViewById(R.id.attendannce);
        registercomplaint = findViewById(R.id.registercomplaint);
        results = findViewById(R.id.results);
        watsoncard = findViewById(R.id.watsoncard);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        profile.setOnClickListener(this);
        chat.setOnClickListener(this);
        attendannce.setOnClickListener(this);
        registercomplaint.setOnClickListener(this);
        results.setOnClickListener(this);
        watsoncard.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile:
                startActivity(new Intent(this, Profile.class));
                break;
            case R.id.chat:
                startActivity(new Intent(this, ChatFac.class));
                break;
            case R.id.attendannce:
                startActivity(new Intent(this, Attendance.class));
                break;
            case R.id.registercomplaint:
                Log.d("----", "reg");
                break;
            case R.id.results:
                Log.d("----", "ressss");
                break;
            case R.id.watsoncard:
                startActivity(new Intent(this, Watson.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("logincheck", false);
                editor.apply();
                finish();
                return true;
            default:
                return false;
        }
    }
}
