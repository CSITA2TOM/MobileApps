package com.example.noteapp;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tool.SimpleCookieJar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {

    ImageView signup_return;
    LinearLayout signup_create;
    EditText edEmail, edPassword, edName;
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SimpleCookieJar cookieJar = new SimpleCookieJar(getApplicationContext());
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        initComponent();
    }
    private void initComponent() {
        signup_return = (ImageView) findViewById(R.id.signup_return);
        signup_create = (LinearLayout) findViewById(R.id.signup_create);
        edEmail = (EditText) findViewById(R.id.signup_email);
        edPassword = (EditText) findViewById(R.id.signup_password);
        edName = (EditText) findViewById(R.id.signup_name);

        signup_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signup_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edName.getText().toString().equals("")){
                    Toast.makeText(SignUp.this, "Username can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edEmail.getText().toString().equals("")){
                    Toast.makeText(SignUp.this, "E-mail can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edPassword.getText().toString().equals("")){
                    Toast.makeText(SignUp.this, "Password can not be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = edEmail.getText().toString();
                String name = edName.getText().toString();
                String password = edPassword.getText().toString();
                MediaType mediaType = MediaType.parse("application/json");
                String jsonStr = "";
                try {
                    JSONObject json = new JSONObject();
                    json.put("email", email);
                    json.put("password", password);
                    json.put("name", name);
                    jsonStr = json.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(mediaType, jsonStr);

                Request request = new Request.Builder()
                        .url("http://47.250.83.35:4000/api/v1/auth/register")
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 處理失敗
                        runOnUiThread(() -> {
                            Toast.makeText(SignUp.this, "註冊失敗", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(SignUp.this, "註冊成功，請登入", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, SignIn.class);
                                startActivity(intent);
                            });
                        } else {

                        }
                    }
                });
            }
        });
    }
}