package com.example.tool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.noteapp.R;

import java.util.ArrayList;

public class PhotoListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PhotoModel> mData; // 使用 Contact 对象列表

    public PhotoListAdapter(Context context, ArrayList<PhotoModel> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItemView = inflater.inflate(R.layout.list_photo_item, parent, false);
        }

        PhotoModel currentItem = (PhotoModel) getItem(position);

        TextView txtLat = listItemView.findViewById(R.id.txt_photo_lat);
        TextView txtLon = listItemView.findViewById(R.id.txt_photo_lon);
        TextView txtTime = listItemView.findViewById(R.id.txt_photo_time);
        ImageView img = listItemView.findViewById(R.id.img_photo);

        txtLat.setText("Lat: " + currentItem.getLat());
        txtLon.setText("Lon: " + currentItem.getLon());
        txtTime.setText(currentItem.getUpdateTime());
        txtTime.setText(currentItem.getUpdateTime());

        Glide.with(mContext)
                .load(currentItem.getImgPath())
                .into(img);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick((PhotoModel) getItem(position));
                }
            }
        });

        return listItemView;
    }

    public interface OnItemClickListener {
        void onItemClick(PhotoModel contact);
    }


    private PhotoListAdapter.OnItemClickListener mListener; // 监听器

    public void setOnItemClickListener(PhotoListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
