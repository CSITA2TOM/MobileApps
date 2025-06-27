package com.example.noteapp;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tool.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    LinearLayout btn1,btn2,btn3;
    ViewPager viewPager;
    ImageView img1,img2,img3;
    TextView txt1,txt2,txt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        // 將五個Fragment 存入list 中，方便調用
        List<Fragment> fragments=new ArrayList<Fragment>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());

        // 實例化適配器
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);

        // 設定適配器
        viewPager = (ViewPager)findViewById(R.id.main_viewpager);
        viewPager.setAdapter(adapter);
        // 設定 viewpager 變化的事件
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(0);

        btn1 = (LinearLayout) findViewById(R.id.lin_1);
        btn2 = (LinearLayout) findViewById(R.id.lin_2);
        btn3 = (LinearLayout) findViewById(R.id.lin_3);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

    }

    // 當頁面進行左右滑動時
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position){
            case 0:
                txt1.setTextColor(getColor(R.color.my_blue));
                txt2.setTextColor(getColor(R.color.my_gray));
                txt3.setTextColor(getColor(R.color.my_gray));

                img1.setImageDrawable(getDrawable(R.drawable.icon1s));
                img2.setImageDrawable(getDrawable(R.drawable.icon2));
                img3.setImageDrawable(getDrawable(R.drawable.icon3));
                break;
            case 1:
                txt1.setTextColor(getColor(R.color.my_gray));
                txt2.setTextColor(getColor(R.color.my_blue));
                txt3.setTextColor(getColor(R.color.my_gray));

                img1.setImageDrawable(getDrawable(R.drawable.icon1));
                img2.setImageDrawable(getDrawable(R.drawable.icon2s));
                img3.setImageDrawable(getDrawable(R.drawable.icon3));
                break;
            case 2:
                txt1.setTextColor(getColor(R.color.my_gray));
                txt2.setTextColor(getColor(R.color.my_gray));
                txt3.setTextColor(getColor(R.color.my_blue));

                img1.setImageDrawable(getDrawable(R.drawable.icon1));
                img2.setImageDrawable(getDrawable(R.drawable.icon2));
                img3.setImageDrawable(getDrawable(R.drawable.icon3s));
                break;

            default:
                break;
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onCurrent(int i){
        viewPager.setCurrentItem(i);
    }
}