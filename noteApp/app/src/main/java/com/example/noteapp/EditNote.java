package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tool.NoteListAdapter;
import com.example.tool.NoteModel;
import com.example.tool.SimpleCookieJar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditNote extends AppCompatActivity {

    Button saveBtn, delBtn;
    EditText ed_title, ed_content;
    TextView txt_note_title;
    OkHttpClient client;
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        SimpleCookieJar cookieJar = new SimpleCookieJar(getApplicationContext());
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        ed_content = (EditText) findViewById(R.id.ed_note_content);
        ed_title = (EditText) findViewById(R.id.ed_note_title);
        saveBtn = (Button) findViewById(R.id.save_note);
        delBtn = (Button) findViewById(R.id.del_note);
        txt_note_title = (TextView) findViewById(R.id.txt_note_title);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id == null || id.isEmpty()) {
            id = "";
            delBtn.setVisibility(View.GONE);
        } else{
            txt_note_title.setText("編輯筆記");
        }

        if(id.length() > 1){
            Request request = new Request.Builder()
                    .url("http://47.250.83.35:4000/api/v1/article/" + id)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 處理失敗
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String jsonString = response.body().string();
                            JSONObject rootObject = new JSONObject(jsonString);
                            JSONObject dataObject = rootObject.getJSONObject("data");
                            String title = dataObject.getString("title");
                            String content = dataObject.getString("content");

                            runOnUiThread(() -> {
                                ed_title.setText(title);
                                ed_content.setText(content);
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                }
            });
        }

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder()
                        .url("http://47.250.83.35:4000/api/v1/article/" + id)
                        .delete()
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(EditNote.this, "操作成功", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }
                    }
                });
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_title.getText().toString().equals("")){
                    Toast.makeText(EditNote.this, "標題不能為空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ed_content.getText().toString().equals("")){
                    Toast.makeText(EditNote.this, "內容不能為空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String title = ed_title.getText().toString().trim();
                String content = ed_content.getText().toString().trim();
                MediaType mediaType = MediaType.parse("application/json");
                String json = "{\"title\":\"" + title + "\",\"content\":\"" + content + "\"}";
                RequestBody body = RequestBody.create(mediaType, json);
                Request request;
                if(id.length() > 1){
                    request = new Request.Builder()
                            .url("http://47.250.83.35:4000/api/v1/article/"+id)
                            .post(body)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url("http://47.250.83.35:4000/api/v1/article")
                            .post(body)
                            .build();
                }

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 處理失敗
                        runOnUiThread(() -> {
                            Toast.makeText(EditNote.this, "操作失敗", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(EditNote.this, "操作成功", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        } else {

                        }
                    }
                });

            }
        });
    }
}