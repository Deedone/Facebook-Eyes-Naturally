package com.feyes.facebookeyes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.feyes.facebookeyes.controller.Show;
import com.feyes.facebookeyes.ssp.SpeechController;
import com.feyes.facebookeyes.ssp.UserAction;
import com.feyes.facebookeyes.ssp.sphinx.SpeechControllerSphinx;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.feyes.facebookeyes.R.id.fill;
import static com.feyes.facebookeyes.R.id.text_all;

public class MainActivity extends AppCompatActivity {

    public static String login;
    public static String password;
	TextView edtAll;
    public static SpeechController speechController;

    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		mainActivity = this;
		setContentView(R.layout.activity_main);
		// printKeyHash();
		edtAll = (TextView)findViewById(R.id.text_all);
		edtAll.setMovementMethod(new ScrollingMovementMethod());
		int permissionCheck = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.RECORD_AUDIO);

		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			throw new StackOverflowError();
        } else {
//        	runOnUiThread(this);
		}

        //
        final Button btnStart = (Button) findViewById(R.id.button_start);
        final Button btnStop = (Button) findViewById(R.id.button_stop);
        final Button btnLogin = (Button) findViewById(R.id.button_login);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(
                        new Intent(MainActivity.this, WorkInBackground.class));
            }
        });

        // запуск службы
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if(speechController != null) {
					speechController.speak("Сила ночи сила дня - одинакова хуйня");
				}
                // используем явный вызов службы
                startService(
                        new Intent(MainActivity.this, WorkInBackground.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editLogin = (EditText)findViewById(R.id.edit_email);
                EditText editPassword = (EditText)findViewById(R.id.edit_password);

                login = editLogin.getText().toString();
                password = editPassword.getText().toString();
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async ta
//				runOnUiThread(this);
            } else {
            	throw new OutOfMemoryError();
//                finish();
            }
        }
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if(speechController != null) {
			speechController.destroy();
		}
	}

	private void printKeyHash() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.feyes.facebookeyes", PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures){

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KetHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
