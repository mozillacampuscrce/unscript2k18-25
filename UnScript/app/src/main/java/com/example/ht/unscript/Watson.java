package com.example.ht.unscript;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Watson extends AppCompatActivity {

    ChatView mChatView;

    Bitmap icon1, icon2;
    String myName = "ht";
    String yourName = "Bot";

    int myId = 0;
    int yourId = 1;

    WatsonUser you, me;

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

    String selectedLanguage = "english";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watson);

        mChatView = findViewById(R.id.msg_view);

        icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person_black_24dp);
        icon2 = BitmapFactory.decodeResource(getResources(), R.drawable.dribblinkbot);

        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        myName = sharedpreferences.getString("rollNo", null);

        if (myName == null)
            myName = "ht";

        me = new WatsonUser(myId, myName, icon1);
        you = new WatsonUser(yourId, yourName, icon2);

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        //mChatView.setBackground(getDrawable(R.drawable.bgmin));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan900));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("type a message");
        mChatView.setMessageMarginTop(2);
        mChatView.setMessageMarginBottom(2);

        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });

    }

    public void sendmessage(){

        final ConversationService myConversationService = new ConversationService("2017-05-26", getString(R.string.username), getString(R.string.password));

        //new message
        com.github.bassaer.chatmessageview.model.Message message = new com.github.bassaer.chatmessageview.model.Message.Builder()
                .setUser(me)
                .setRightMessage(true)
                .setMessageText(mChatView.getInputText())
                .hideIcon(true)
                .build();
        //Set to chat view
        mChatView.send(message);
        // Optionally, clear edittext
        MessageRequest request = new MessageRequest.Builder()
                .inputText(message.getMessageText())
                .build();
        mChatView.setInputText("");

        myConversationService.message(getString(R.string.workspace), request).enqueue(new ServiceCallback<MessageResponse>() {
            @Override
            public void onResponse(MessageResponse response) {
                // More code here
                final String outputText = response.getText().get(0);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (selectedLanguage.equals("english")) {
                            final com.github.bassaer.chatmessageview.model.Message receivedMessage = new com.github.bassaer.chatmessageview.model.Message.Builder()
                                    .setUser(you)
                                    .setRightMessage(false)
                                    .setMessageText(outputText)
                                    .build();
                            Log.d("----", outputText);
                            mChatView.receive(receivedMessage);
                        }
                        else
                            transText(outputText);
                    }
                });

                if(response.getIntents().get(0).getIntent().endsWith("RequestQuote")) {
                    // More code here
                }
            }

            @Override
            public void onFailure(Exception e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    private void transText(final String outputText){

        final String url = getString(R.string.urltranslate);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {


                postdata = new JSONObject();
                try {
                    postdata.put("text", outputText);
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
                
                String out = null;

                try {
                    resultData = new JSONObject(result);
                    out = resultData.getString("text");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ApiSucess(out);

            }

        }.execute();
    }
    private void ApiSucess(String outputText) {

        final com.github.bassaer.chatmessageview.model.Message receivedMessage = new com.github.bassaer.chatmessageview.model.Message.Builder()
                .setUser(you)
                .setRightMessage(false)
                .setMessageText(outputText)
                .build();
        Log.d("----", outputText);
        mChatView.receive(receivedMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.language, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.english:
                selectedLanguage = "english";
                return true;
            case R.id.hindi:
                selectedLanguage = "hindi";
                return true;
            default:
                return false;
        }
    }
}
