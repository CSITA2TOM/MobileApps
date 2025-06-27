package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tool.SimpleCookieJar;

import okhttp3.OkHttpClient;

public class Fragment3 extends Fragment {

    private SimpleCookieJar cookieJar;
    Button btnLogout;
    TextView txt_username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_fragment3, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        btnLogout = getView().findViewById(R.id.btn_logout);
        txt_username = getView().findViewById(R.id.txt_username);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 初始化 CookieJar
                cookieJar = new SimpleCookieJar(getActivity());
                if (cookieJar != null) {
                    cookieJar.clearCookies();
                }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("name");
                editor.commit();
                Intent intent = new Intent(getActivity(), Welcome.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String name= sharedPreferences.getString("name","");
        txt_username.setText(name);
    }
}