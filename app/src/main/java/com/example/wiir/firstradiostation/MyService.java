package com.example.wiir.firstradiostation;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.net.Uri;
//import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;

public class MyService extends Service{

    private MediaPlayer mediaPlayer2 = new MediaPlayer();
    private String url = "http://142.4.192.46:7070";

    private boolean prepared= false;



    @Override
    public IBinder onBind(Intent intent) { // se debe implementar por ser un metodo abstracto, aunque no se use
        return null;
    }

    @Override
    public void onCreate() {

        Toast.makeText(this, "Service created!", Toast.LENGTH_SHORT).show();

        mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //new TaskPlayer().execute(url);

        try {
            mediaPlayer2.setDataSource(url);
            mediaPlayer2.prepare();
            prepared = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show();

        mediaPlayer2.start();
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed!", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        //mediaPlayer2.release();

    }



    /*private class TaskPlayer extends AsyncTask <String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer2.setDataSource(strings[0]);
                mediaPlayer2.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }*/
}
