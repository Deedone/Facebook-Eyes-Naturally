package com.feyes.facebookeyes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.ViewDebug;
import android.widget.Toast;

import com.feyes.facebookeyes.controller.Close;
import com.feyes.facebookeyes.controller.Open;
import com.feyes.facebookeyes.controller.Show;
import com.feyes.facebookeyes.controller.StandardAction;
import com.feyes.facebookeyes.controller.Stop;
import com.feyes.facebookeyes.ssp.UserAction;
import com.feyes.facebookeyes.ssp.sphinx.SpeechControllerSphinx;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

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

    private void onTextContent() {
    	new Close();
    	new Show();
    	new Open();
    	new Stop();

    	UserAction[] actions = new UserAction[StandardAction.actions.size()];

		MainActivity.speechController =
				new SpeechControllerSphinx(MainActivity.mainActivity, StandardAction.actions.toArray(actions), 1e-30f);

		SpeechControllerSphinx.logger = new PrintWriter(new Writer() {
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {

				final String s = new String(cbuf, off, len);

				System.err.print(s);
				MainActivity.mainActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						MainActivity.mainActivity.edtAll.append(s);
					}
				});
			}

			@Override
			public void flush() throws IOException {

			}

			@Override
			public void close() throws IOException {

			}
		});

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
      //  mPlayer.stop();
    }
}
