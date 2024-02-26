package com.example.myapplication.model;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.Support.User;

import java.util.ArrayList;
import java.util.List;
public class UserAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private ArrayList<User> mUserList;

    public UserAdapter(Context context, ArrayList<User> userList) {
        super(context, 0, userList);
        mContext = context;
        mUserList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.drop_down_list, parent, false);
        }
        Log.d(TAG, "getView: position "+position+" "+mUserList.size());
        User user1=getItem(position);
        // Add a null check and ensure mUserList is not empty)
            User user =getItem(position);
            TextView textView = view.findViewById(R.id.textview_bottom);
            textView.setText(user.getUsername());
            TextView textView1 = view.findViewById(R.id.textview_bottom_small);
            textView1.setText(String.valueOf(user.getUser_id())); // Or any other user information you want to display

        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}

