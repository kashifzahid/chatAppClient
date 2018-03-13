package com.example.hahahaha.chatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class socketActivity extends AppCompatActivity  {
private Socket socket;

    {
        try{
            socket= IO.socket("http://192.168.10.3:8080");

        }catch (URISyntaxException e){
            throw new RuntimeException(e);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        socket.connect();
    }
    public void onDestroy(){
        super.onDestroy();
        socket.disconnect();
    }
    public void sendMessage (String inp) {




        socket.emit("new message", inp);
        Log.i("success","message sent successfully");





    }
}
