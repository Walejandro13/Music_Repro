package com.android.alejandrov.musicrepro;

import android.content.ComponentName;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.ServiceConnection;


public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private Button play,stop, next, prev;
    private MediaPlayer mediaplayer;
    private TextView time;
    private ProgressBar progrestime;
    private Reproductor reproductor;
    final int HOUR = 60*60*1000;
    final int MINUTE = 60*1000;
    final int SECOND = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.btn_play);
        play.setOnClickListener(this);
        stop = (Button) findViewById(R.id.btn_stop);
        stop.setOnClickListener(this);
        next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(this);
        prev = (Button) findViewById(R.id.btn_prev);
        prev.setOnClickListener(this);
        play.setEnabled(true);
        stop.setEnabled(false);

        time= (TextView) findViewById(R.id.tv_time);

        int durationInMillis = Reproductor.duration;
        int durationHour = durationInMillis/HOUR;
        int durationMint = (durationInMillis%HOUR)/MINUTE;
        int durationSec = (durationInMillis%MINUTE)/SECOND;


        time.setText(String.format("%02d:%02d:%02d",durationHour,durationMint,durationSec));
    }

    private ServiceConnection conn = new ServiceConnection() {
        String TAG = "ServiceConnection";

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            reproductor = ((Reproductor.Reprobinder) iBinder).getService();

            Log.i(TAG, "onServiceConnected: ");


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected: ");
        }
    };


    @Override
    public void onClick(View v) {
        Intent startIntent = new Intent(MainActivity.this, Reproductor.class);
        switch (v.getId()) {
            case (R.id.btn_play):

                startIntent.setAction("Play track Intent");
                startService(startIntent);

                play.setEnabled(false);
                stop.setEnabled(true);


            break;
            case (R.id.btn_stop):
                Intent stopIntent = new Intent(MainActivity.this, Reproductor.class);
                stopIntent.setAction("Stop action");
                startService(stopIntent);
                play.setEnabled(true);
                stop.setEnabled(false);
                break;
            case (R.id.btn_next):

                startIntent.setAction("Next track intent");
                startService(startIntent);

                if (!play.isEnabled()){
                    play.setEnabled(false);
                    stop.setEnabled(true);
                }
                else {
                    stop.setEnabled(false);
                    play.setEnabled(true);
                }

                break;
            case (R.id.btn_prev):

                startIntent.setAction("Previous track intent");
                startService(startIntent);

                if (!play.isEnabled()){
                    play.setEnabled(false);
                    stop.setEnabled(true);
                }
                else {
                    stop.setEnabled(false);
                    play.setEnabled(true);
                }

                break;

        }

    }
    //TODO implementar progress bar i conectar actividad con servicio
    /*protected void setProgressText() {

        final int HOUR = 60*60*1000;
        final int MINUTE = 60*1000;
        final int SECOND = 1000;

        int durationInMillis = Reproductor.duration;
       // int curVolume = mp.getCurrentPosition();

        int durationHour = durationInMillis/HOUR;
        int durationMint = (durationInMillis%HOUR)/MINUTE;
        int durationSec = (durationInMillis%MINUTE)/SECOND;

        int currentHour = curVolume/HOUR;
        int currentMint = (curVolume%HOUR)/MINUTE;
        int currentSec = (curVolume%MINUTE)/SECOND;

        if(durationHour>0){
            System.out.println(" 1 = "+String.format("%02d:%02d:%02d/%02d:%02d:%02d",
                    currentHour,currentMint,currentSec, durationHour,durationMint,durationSec));
        }else{
            System.out.println(" 1 = "+String.format("%02d:%02d/%02d:%02d",
                    currentMint,currentSec, durationMint,durationSec));
        }
    }*/
}
