package com.feyes.facebookeyes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.ViewDebug;
import android.widget.Toast;

public class WorkInBackground extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Служба создана",
                Toast.LENGTH_SHORT).show();
    //    mPlayer = MediaPlayer.create(this, R.raw.flower_romashka);
    //    mPlayer.setLooping(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Служба запущена",
                Toast.LENGTH_SHORT).show();
       // mPlayer.start();
        onTextContent();
        return super.onStartCommand(intent, flags, startId);
    }

    private void onTextContent(){

        for (int i = 0 ;i < 10; i++)
            Toast.makeText(this, Integer.toString(i),
                    Toast.LENGTH_SHORT).show();

        Toast.makeText(this,MainActivity.login,
                Toast.LENGTH_SHORT).show();
        Toast.makeText(this, MainActivity.password,
                Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
      //  mPlayer.stop();
    }
}
