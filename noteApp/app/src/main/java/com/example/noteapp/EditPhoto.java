package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tool.SimpleCookieJar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditPhoto extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS = 100;

    private ImageView imageView;
    private TextView tvLocation;
    private Uri photoURI;
    Button save_photo, delBtn;
    File photoFile = null;
    private FusedLocationProviderClient fusedLocationClient;
    OkHttpClient client;
    String lat, lon;
    String id;
    TextView txt_photo_title;
    SimpleCookieJar cookieJar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        cookieJar = new SimpleCookieJar(getApplicationContext());
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);
        imageView = findViewById(R.id.iv_photo);
        tvLocation = findViewById(R.id.tv_location);
        save_photo = findViewById(R.id.save_photo);
        txt_photo_title = findViewById(R.id.txt_note_title);
        delBtn = findViewById(R.id.del_photo);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id == null || id.isEmpty()) {
            id = "";
            delBtn.setVisibility(View.GONE);
        } else{
            txt_photo_title.setText("查看照片");
            save_photo.setVisibility(View.GONE);
            btnTakePhoto.setVisibility(View.GONE);
        }

        if(id.length() > 1){
            Request request = new Request.Builder()
                    .url("http://47.250.83.35:4000/api/v1/photo/" + id)
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
                            String imgPath = dataObject.getString("imgPath");
                            String coordinate = dataObject.getString("coordinate");
                            lat = coordinate.split(",")[0];
                            lon = coordinate.split(",")[1];
                            String latLng = "緯度: " + lat + "  經度: " + lon;


                            runOnUiThread(() -> {
                                        tvLocation.setText(latLng);
                                Glide.with(EditPhoto.this)
                                        .load(imgPath)
                                        .into(imageView);
                            });
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 處理未授權等情況
                    }
                }
            });
        }

        btnTakePhoto.setOnClickListener(view -> {
            if (checkAndRequestPermissions()) {
                openCamera();
                getLocation();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder()
                        .url("http://47.250.83.35:4000/api/v1/photo/" + id)
                        .delete()
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 處理失敗
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(EditPhoto.this, "操作成功", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }
                    }
                });
            }
        });

        save_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoFile != null && photoFile.exists()) {
                    uploadImage(photoFile);
                } else {
                    Toast.makeText(EditPhoto.this, "請先拍照", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean checkAndRequestPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissionsNeeded.add(Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), REQUEST_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = File.createTempFile("IMG_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException ex) {
                Toast.makeText(this, "無法建立照片檔案", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        String latLng = "緯度: " + location.getLatitude() + "\n經度: " + location.getLongitude();
                        lat = location.getLatitude() + "";
                        lon = location.getLongitude() + "";
                        tvLocation.setText(latLng);
                    } else {
                        tvLocation.setText("無法取得位置");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView.setImageURI(photoURI);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (checkAndRequestPermissions()) {
                openCamera();
                getLocation();
            } else {
                Toast.makeText(this, "需要相應權限才能使用此功能", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void uploadImage(File imageFile) {

        MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageFile.getName(),
                        RequestBody.create(imageFile, MEDIA_TYPE_JPG))
                .build();

        Request request = new Request.Builder()
                .url("http://47.250.83.35:4000/api/v1/photo")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(EditPhoto.this, "上傳失敗: "+ e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        try {
                            String jsonData =  response.body().string();
                            JSONObject rootObject = new JSONObject(jsonData);
                            JSONObject dataObject = rootObject.getJSONObject("data");
                            String imgPath = dataObject.getString("fileName");

                            MediaType mediaType = MediaType.parse("application/json");
                            String latLon = lat + ',' + lon;
                            String json = "{\"imgPath\":\"" + imgPath + "\",\"coordinate\":\"" + latLon + "\"}";
                            RequestBody body = RequestBody.create(mediaType, json);

                            Request request2 = new Request.Builder()
                                    .url("http://47.250.83.35:4000/api/v1/photo/link-image-coordinate")
                                    .post(body)
                                    .build();
                            client.newCall(request2).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        runOnUiThread(() -> {
                                            try {
                                                Toast.makeText(EditPhoto.this, "上傳成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    } else {

                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}