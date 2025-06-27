package com.example.tool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.noteapp.R;

import java.util.ArrayList;

public class NoteListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NoteModel> mData;

    public NoteListAdapter(Context context, ArrayList<NoteModel> data) {
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
            listItemView = inflater.inflate(R.layout.list_note_item, parent, false);
        }

        NoteModel currentItem = (NoteModel) getItem(position);

        TextView txtTitle = listItemView.findViewById(R.id.item_note_title);
        TextView txtDesc = listItemView.findViewById(R.id.item_note_desc);
        TextView txtTime = listItemView.findViewById(R.id.item_note_time);

        txtTitle.setText(currentItem.getTitle());
        txtDesc.setText(currentItem.getContent());
        txtTime.setText(currentItem.getUpdateTime());

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick((NoteModel) getItem(position));
                }
            }
        });

        return listItemView;
    }

    public interface OnItemClickListener {
        void onItemClick(NoteModel contact);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
