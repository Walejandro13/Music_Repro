package com.android.alejandrov.musicrepro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.media.MediaPlayer;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.ActivityManager;


public class Reproductor extends Service {
    private static String TAG= "Reproductor service running ";
    private MediaPlayer mp;
    private int[] sounds;
    public static int i =0,opc=1, duration;
    private Notification notification= null;




    public Reproductor() {
    }
    public class Reprobinder extends Binder {
        public Reproductor getService(){
            return Reproductor.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        sounds = new int[]{R.raw.byebye, R.raw.firstdate, R.raw.power};

        mp = MediaPlayer.create(this, sounds[i]);
        duration = mp.getDuration();
        Log.i(TAG, "onCreate: la duracion de la cancion es:"+ duration );


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent.getAction().equals("Play track Intent")) {

            mp.start();
            reconstrudNot(1);
            duration= mp.getDuration();

            opc=1;

            if (mp.getCurrentPosition()==duration){
                mp = MediaPlayer.create(this, sounds[i++]);
                mp.start();
            }
            //Todo necesario pasar a otra cancion




        }
        else if (intent.getAction().equals("Stop action")){
            mp.stop();
            stopForeground(true);
            stopSelf();

           // reconstrudNot(2);

        }
        else if (intent.getAction().equals("Next track intent")){
            if ((i<3)&& mp.isPlaying() && i!=2){
                mp.stop();
                mp = MediaPlayer.create(this, sounds[++i]);
                mp.start();
            } else if ((i < 3) && mp.isPlaying()==false) {
                mp = MediaPlayer.create(this, sounds[++i]);

            } else if (i == 2) {
                i = 0;
                mp.stop();
                mp = MediaPlayer.create(this, sounds[i]);
                Toast.makeText(this, "Fin de la lista",
                        Toast.LENGTH_SHORT).show();
                //mp.start();
            }

        }
        else if (intent.getAction().equals("Previous track intent")) {
            if ((i < 3)&&(i>0) && mp.isPlaying()) {
                mp.stop();
                mp = MediaPlayer.create(this, sounds[--i]);
                mp.start();
            }else if ((i < 3)&&(i>0) && !mp.isPlaying()){
                mp = MediaPlayer.create(this, sounds[--i]);
            }


        }
        else if (intent.getAction().equals("Pause track intent")) {
            mp.pause();
            reconstrudNot(2);
            opc=2;

        }


            return START_STICKY;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this,"Servicio detenido",
          //      Toast.LENGTH_SHORT).show();
        mp.stop();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
    //    throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    public void reconstrudNot(int opc){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction("Intent Notification");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this,Reproductor.class);
        previousIntent.setAction("Previous track intent");
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, Reproductor.class);
        playIntent.setAction("Play track Intent");
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, Reproductor.class);
        nextIntent.setAction("Next track intent");
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent pauseIntent = new Intent(this, Reproductor.class);
        nextIntent.setAction("Pause track intent");
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon);

        if (opc==1){
         notification = new NotificationCompat.Builder(this)
                .setContentTitle("Mi reproductor")
                .setTicker("Truiton Music Player")
                .setContentText("Reproductor notification").setSmallIcon(R.drawable.icon )
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 64, 64, false))
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .addAction(android.R.drawable.ic_media_previous,
                        "Previous", ppreviousIntent)
                .addAction(android.R.drawable.ic_media_pause, "Pause", //voy a cambiar de play a stop o pause
                        ppauseIntent)
                .addAction(android.R.drawable.ic_media_next, "Next",
                        pnextIntent).

                        build();
        startForeground(10,notification);
        }
        else if (opc==2){
           notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Mi reproductor")
                    .setTicker("Truiton Music Player")
                    .setContentText("Reproductor notification").setSmallIcon(R.drawable.icon )
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 64, 64, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .addAction(android.R.drawable.ic_media_previous,
                            "Previous", ppreviousIntent)
                    .addAction(android.R.drawable.ic_media_play, "Play", //voy a cambiar de play a stop o pause
                            pplayIntent)
                    .addAction(android.R.drawable.ic_media_next, "Next",
                            pnextIntent).

                            build();
            startForeground(10,notification);
        }

       // return notification;
    }
}

