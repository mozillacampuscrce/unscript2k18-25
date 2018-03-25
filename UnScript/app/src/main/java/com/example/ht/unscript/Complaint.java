package com.example.ht.unscript;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class Complaint extends AppCompatActivity {

    public static final String storageConnectionString = "DefaultEndpointsProtocol=https;"
            + "AccountName=hackathonpdf;"
            + "AccountKey=n01ALbUky6U4LSotdpOvs/GSimmLhO9wzXLFuTnqp5IVeFhMcFFJRQSsRMWPGHLznoiQK0GZ+VbaBhx8pslJ5g==";

    String filename = "abc.jpg";
    String FilePath;

    File file;

    TextView textFile;

    private static final int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        Button buttonPick = (Button)findViewById(R.id.buttonpick);
        textFile = (TextView)findViewById(R.id.textfile);

        buttonPick.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("*/*");
                //startActivityForResult(intent,PICKFILE_RESULT_CODE);

                file = new File(Environment.getExternalStorageDirectory() + "/CitizenService/", filename);

                final Handler handler = new Handler();

                Thread th = new Thread(new Runnable() {
                    public void run() {

                        CloudStorageAccount storageAccount = null;
                        try {
                            storageAccount = CloudStorageAccount.parse(storageConnectionString);
                            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
                            // Get a reference to a container.
                            // The container name must be lower case
                            CloudBlobContainer container = blobClient.getContainerReference("audio");
                            container.createIfNotExists();

                            //Getting a blob reference
                            CloudBlockBlob blob = container.getBlockBlobReference(filename);

                            blob.uploadFromFile(file.toString());

                            Log.i("----", file.getAbsolutePath());


                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (StorageException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }});

                th.start();

                // Create the blob client.

            }});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){

                    data.getDataString();
                    Uri selectedImageUri = data.getData();

                    Uri uri = Uri.fromFile(new File(String.valueOf(selectedImageUri)));
                    //FilePath = data.getData().getPath();


                    textFile.setText(data.getDataString());
                }
                break;
        }
    }
}
