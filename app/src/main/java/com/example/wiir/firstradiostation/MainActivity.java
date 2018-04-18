package com.example.wiir.firstradiostation;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {


    Button b_play, b_play_s, b_stop_s;

    MediaPlayer mediaPlayer;

    boolean prepared= false;
    boolean started= false;

    String stream="http://142.4.192.46:7070";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_play_s = findViewById(R.id.b_play_s);
        b_stop_s = findViewById(R.id.b_stop_s);

        b_play_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, MyService.class));
            }
        });
        b_stop_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopService(new Intent(MainActivity.this, MyService.class));
            }
        });




        b_play = (Button) findViewById(R.id.b_play);
        b_play.setEnabled(false);
        b_play.setText("LOADING...");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer.setAudioAttributes(new AudioAttributes.Builder() .setUsage(AudioAttributes.USAGE_MEDIA) .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN) .build());
        //mediaPlayer.setAudioAttributes(new AudioAttributes.Builder() .setUsage(AudioAttributes.USAGE_MEDIA) .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN) .build());
        new PlayerTask().execute(stream);//Se llama para ejecutar prepare() del objeto tipo MediaPlayer
        

            b_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (started) {
                        started=false;
                        mediaPlayer.pause();
                        b_play.setText("PLAY WIIR");
                    } else {
                        started=true;
                        mediaPlayer.start();
                        b_play.setText("PAUSE");
                    }
                }
            });
        }

    class PlayerTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            b_play.setEnabled(true);
            b_play.setText("PLAY WIIR");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (started){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prepared){
            mediaPlayer.release();
        }
    }
}
