package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tool.SimpleCookieJar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignIn extends AppCompatActivity {
    ImageView signin_return;
    EditText edEmail, edPassword;
    LinearLayout signin_btn;

    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SimpleCookieJar cookieJar = new SimpleCookieJar(getApplicationContext());
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        initComponent();

    }

    private void initComponent() {
        signin_return = (ImageView) findViewById(R.id.signin_return);
        signin_btn = (LinearLayout) findViewById(R.id.signin_btn);

        edEmail = (EditText) findViewById(R.id.signin_email);
        edPassword = (EditText) findViewById(R.id.signin_password);

        signin_btn.setOnClickListener(v -> {
            if(edEmail.getText().toString().equals("")){
                Toast.makeText(SignIn.this, "E-mail can not be empty！", Toast.LENGTH_SHORT).show();
                return;
            }
            if(edPassword.getText().toString().equals("")){
                Toast.makeText(SignIn.this, "Password can not be blank！", Toast.LENGTH_SHORT).show();
                return;
            }
            String email = edEmail.getText().toString();
            String password = edPassword.getText().toString();

            MediaType mediaType = MediaType.parse("application/json");
            String json = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
            RequestBody body = RequestBody.create(mediaType, json);

            Request request = new Request.Builder()
                    .url("http://47.250.83.35:4000/api/v1/auth/login")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 處理失敗
                    runOnUiThread(() -> {
                        Toast.makeText(SignIn.this, "登入失敗,請檢查帳號密碼", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            try {
                                String jsonString = response.body().string();
                                JSONObject rootObject = new JSONObject(jsonString);

                                JSONObject userObject = rootObject.getJSONObject("user");

                                String name = userObject.getString("name");
                                SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name", name);
                                editor.commit();

                                Toast.makeText(SignIn.this, "登入成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignIn.this, MainActivity.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        });
                    } else {

                    }
                }
            });
        });

        signin_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}