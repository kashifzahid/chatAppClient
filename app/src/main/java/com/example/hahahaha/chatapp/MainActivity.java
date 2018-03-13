package com.example.hahahaha.chatapp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Socket socket;
    private EditText minput;
    private Button send;
    private socketActivity sockets;
    private EditText userName;
    private Button connect;
    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {
            try{
                socket = IO.socket("http://192.168.10.3:8080");
            }catch(URISyntaxException e){
                throw new RuntimeException();
            }
        }
        minput = findViewById(R.id.editText);
        minput.addTextChangedListener(onTextChangedListener());


        send = findViewById(R.id.button);

        userName=findViewById(R.id.editText3);
        connect=findViewById(R.id.user);
        text=findViewById(R.id.textView);

        socket.on("new_msg", onNewMessage);
        final String user=userName.getText().toString().trim();
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                        JSONObject room=new JSONObject();
                        {
                            try{
                                room.put("userName",user);
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        socket.emit("join", room);
                        Log.i("room","success joined room");




            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                socket.connect();
                Log.i("success", "Connected successfully");
                //socket.on("message", incoming);
            }
        }, 5000);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String msg = minput.getText().toString().trim();

                sendMessage(msg,user);

            }
        });


    }


    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    public void sendMessage(String m,String u) {
        final String msg=m;

         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                JSONObject data = new JSONObject();
                {
                    try{
                        data.put("message",msg);

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                String message;


                socket.emit("message", data);
                Log.i("success", "message sent successfully");
                minput.setText("");

            }
        }, 5000);
    }

    
    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text.setText("btyping");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      text.setText("typing");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                text.setText("atyping");
            }

        };

    }

    private Emitter.Listener onNewMessage;

    {
        onNewMessage = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                // new Handler().postDelayed(new Runnable() {
                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String messages = null;
                        String users = null;
                        String id = null;

                        try {
                            messages = data.getString("message").toString();
                            users = data.getString("user").toString();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i(" By:" + users, messages);


                    }

                });
            }

        };
    }

}

