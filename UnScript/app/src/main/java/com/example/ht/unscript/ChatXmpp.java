package com.example.ht.unscript;

import android.content.Context;
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
import android.view.View;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.view.ChatView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.Collection;

public class ChatXmpp extends AppCompatActivity {

    XMPPTCPConnectionConfiguration config;
    AbstractXMPPConnection conn1;
    ChatManager chatManager, chatManagerSend;
    ChatManagerListener chatManagerListener;
    ChatMessageListener chatMessageListener;
    Chat send, testChat;

    // Pref
    public static final String pref = "UserData" ;
    SharedPreferences sharedpreferences;
    String jabberId = "";

    // ui
    ChatView mChatView;

    Bitmap icon1, icon2;
    String myName = "ht";
    String yourName = "admin";

    int myId = 0;
    int yourId = 1;

    ChatXmppUser you, me;

    String AJabberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_xmpp);

        mChatView = findViewById(R.id.msg_view);

        sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        jabberId = sharedpreferences.getString("jabberId", null);
        AJabberId = sharedpreferences.getString("AJabberId", null);

        //jabberId = "hardikthakor.ht";

        icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person_black_24dp);
        icon2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person_black_24dp);

        me = new ChatXmppUser(myId, myName, icon1);
        you = new ChatXmppUser(yourId, yourName, icon2);

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

                testChat = chatManagerSend.createChat(AJabberId + "@localhost", new ChatMessageListener() {

                    @Override
                    public void processMessage(Chat chat, final Message message) {

                    }
                });

                //new message
                com.github.bassaer.chatmessageview.model.Message message = new com.github.bassaer.chatmessageview.model.Message.Builder()
                        .setUser(me)
                        .setRightMessage(true)
                        .setMessageText(mChatView.getInputText())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                mChatView.send(message);
                try {
                    testChat.sendMessage(message.getMessageText());
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                //Reset edit text
                mChatView.setInputText("");

            }

        });

        MyLoginTask task = new MyLoginTask();
        task.execute("");
    }

    public class MyLoginTask extends AsyncTask<String, String, String> {
        MainActivity m = new MainActivity();

        @Override
        protected String doInBackground(String... strings) {

            config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(jabberId, "12345")
                    .setHost("54.200.135.120")
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setServiceName("localhost")
                    .setPort(5222)
                    .setDebuggerEnabled(true) // to view what's happening in detail
                    .build();

            Log.i("-------------------", "in");

            conn1 = new XMPPTCPConnection(config);

            try {

                conn1.connect();
                if (conn1.isConnected()) {
                    Log.i("------------------app", "conn done");

                    conn1.login();
                    //setListner();
                    Roster roster = Roster.getInstanceFor(conn1);

                    roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);

                    //roster.createEntry("testandroid@localhost", conn1.getUser(), new String[]{"Buddies"});
                    //roster.createEntry("testandroid@localhost", conn1.getUser(), null);

                    if (!roster.isLoaded()) {
                        try {
                            roster.reloadAndWait();
                        } catch (SmackException.NotLoggedInException e) {
                            e.printStackTrace();
                        } catch (SmackException.NotConnectedException e) {
                            e.printStackTrace();
                        }
                    }
                    Collection<RosterEntry> entries = roster.getEntries();
                    Log.i("--------roaster" , String.valueOf(entries));


                    if(conn1.isAuthenticated()) {
                        chatManagerSend = ChatManager.getInstanceFor(conn1);

                        chatManager = ChatManager.getInstanceFor(conn1);

                        chatManager.addChatListener(new ChatManagerListener() {
                            @Override
                            public void chatCreated(Chat chat, boolean createdLocally) {

                                chat.addMessageListener(new ChatMessageListener() {
                                    @Override
                                    public void processMessage(Chat chat, final Message message) {

                                        Log.i("-------------lastrecv", message.getBody());

                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {

                                                final com.github.bassaer.chatmessageview.model.Message receivedMessage = new com.github.bassaer.chatmessageview.model.Message.Builder()
                                                        .setUser(you)
                                                        .setRightMessage(false)
                                                        .setMessageText(message.getBody())
                                                        .build();
                                                mChatView.receive(receivedMessage);
                                                //Toast.makeText(getBaseContext(), message.getBody(), Toast.LENGTH_SHORT).show();
                                                //tv.setText(message.getBody());
                                                //m.mytest("jknjkbkjjbhb");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    return "ok";
                }
                else {
                    return "not ok";
                }
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            return "last";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //tv.setText(s);
            Toast.makeText(getBaseContext(), "Ready", Toast.LENGTH_SHORT).show();
            Log.i("------------------- ", "out");
            //setListner();
        }
    }

}
