package com.example.ht.unscript;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChatFac extends AppCompatActivity implements View.OnClickListener{

    Button ch1, ch2, ch3, ch4;

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_fac);

        ch1 = findViewById(R.id.ch1);
        ch2 = findViewById(R.id.ch2);
        ch3 = findViewById(R.id.ch3);
        ch4 = findViewById(R.id.ch4);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

        ch1.setOnClickListener(this);
        ch2.setOnClickListener(this);
        ch3.setOnClickListener(this);
        ch4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ch1:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("AJabberId", "snmr");
                editor.apply();
                startActivity(new Intent(this, ChatXmpp.class));
                break;
            case R.id.ch2:
                SharedPreferences.Editor editor2 = sharedpreferences.edit();
                editor2.putString("AJabberId", "cc");
                editor2.apply();
                startActivity(new Intent(this, ChatXmpp.class));
                break;
            case R.id.ch3:
                SharedPreferences.Editor editor3 = sharedpreferences.edit();
                editor3.putString("AJabberId", "ds");
                editor3.apply();
                startActivity(new Intent(this, ChatXmpp.class));
                break;
            case R.id.ch4:
                SharedPreferences.Editor editor4 = sharedpreferences.edit();
                editor4.putString("AJabberId", "bda");
                editor4.apply();
                startActivity(new Intent(this, ChatXmpp.class));
                break;
        }
    }
}
