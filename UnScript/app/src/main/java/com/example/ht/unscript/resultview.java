package com.example.ht.unscript;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class resultview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultview);

        String bda,snmr,cc,ds,result;
        TextView bda1,snmr1,cc1,ds1,result1;
        bda1=findViewById(R.id.bda);
        cc1=findViewById(R.id.cc);
        snmr1=findViewById(R.id.snmr);
        ds1=findViewById(R.id.ds);
        result1=findViewById(R.id.per);

        Bundle b=getIntent().getExtras();
        bda=b.getString("bda");
        cc=b.getString("cc");
        snmr=b.getString("snmr");
        ds=b.getString("ds");
        result=b.getString("percentage");
        Toast.makeText(getBaseContext(),bda,Toast.LENGTH_LONG).show();


        bda1.setText(bda);
        cc1.setText(cc);
        snmr1.setText(snmr);
        ds1.setText(ds);
        result1.setText(result);
    }
}
