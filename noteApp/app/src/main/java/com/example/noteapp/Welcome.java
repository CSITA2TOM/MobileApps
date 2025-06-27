package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tool.SimpleCookieJar;

public class Welcome extends AppCompatActivity {

    LinearLayout lin_signin, lin_signup;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.INTERNET",
            "android.permission.CAMERA"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SimpleCookieJar cookieJar = new SimpleCookieJar(getApplicationContext());
        verifyPermissions(this);
        lin_signin = (LinearLayout) findViewById(R.id.welcome_signin);
        lin_signup = (LinearLayout) findViewById(R.id.welcome_signup);

        if(cookieJar.hasCookies()){
            Intent intent = new Intent(Welcome.this, MainActivity.class);
            startActivity(intent);
        }

        lin_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, SignUp.class);
                startActivity(intent);
            }
        });

        lin_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, SignIn.class);
                startActivity(intent);
            }
        });
    }

    public static void verifyPermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.INTERNET");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}