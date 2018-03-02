package com.example.cblanchard.amio;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    // The task to be periodically executed
    TimerTask task;
    // The timer which executes the task
    Timer timer;

    public MyService() {
    }

    public void onCreate() {
        // Création du service

        final Handler handler = new Handler();
        timer = new Timer(true);
        task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(MyService.this, "Getting web data!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        timer.schedule(task, 0, 5000);
    }

    public void onDestroy() {
        // Destruction du service
        timer.cancel();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // Démarrage du service
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
