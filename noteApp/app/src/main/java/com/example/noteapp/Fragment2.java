package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tool.NoteListAdapter;
import com.example.tool.NoteModel;
import com.example.tool.PhotoListAdapter;
import com.example.tool.PhotoModel;
import com.example.tool.SimpleCookieJar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment2 extends Fragment implements PhotoListAdapter.OnItemClickListener{

    ListView photo_listview;
    OkHttpClient client;
    ArrayList<PhotoModel> photoList = new ArrayList<>();
    TextView add_photo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_fragment2, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponent();
        refreshData();
    }

    public void initComponent() {
        photo_listview = (ListView) getView().findViewById(R.id.photo_listview);
        add_photo = (TextView) getView().findViewById(R.id.add_photo);
        SimpleCookieJar cookieJar = new SimpleCookieJar(getActivity());
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditPhoto.class);
                startActivity(intent);
            }
        });
    }

    public void refreshData() {
        Request request = new Request.Builder()
                .url("http://47.250.83.35:4000/api/v1/photo")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 處理失敗
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    photoList = new ArrayList<>();
                    String responseData = response.body().string();

                    try {
                        JSONObject rootObject = new JSONObject(responseData);
                        JSONArray dataArray = rootObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject item = dataArray.getJSONObject(i);
                            String id = item.getString("_id");
                            String coordinate = item.getString("coordinate");
                            String imgPath = item.getString("imgPath");
                            String lat = coordinate.split(",")[0];
                            String lon = coordinate.split(",")[1];
                            String updatedAt = item.getString("updatedAt");
                            String formattedDate = formatDate(updatedAt);
                            photoList.add(new PhotoModel(id, lat, lon, imgPath, formattedDate));
                        }
                        getActivity().runOnUiThread(() -> {
                            PhotoListAdapter adapter = new PhotoListAdapter(getActivity(), photoList);
                            adapter.setOnItemClickListener(Fragment2.this);
                            photo_listview.setAdapter(adapter);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });

    }

    public String formatDate(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(inputDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onItemClick(PhotoModel photoModel) {
        Intent intent = new Intent(getActivity(), EditPhoto.class);
        intent.putExtra("id", photoModel.getId());
        startActivity(intent);
    }
}
