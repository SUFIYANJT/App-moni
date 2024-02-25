package com.example.myapplication.model;

import static com.example.myapplication.LoginActivity.TAG;

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
import com.example.myapplication.Support.Machine;

import java.util.List;
import java.util.Objects;

public class MachineAdapter extends ArrayAdapter<Machine> {
    private Context mContext;
    private List<Machine> mMachineList;

    public MachineAdapter(Context context, List<Machine> machineList) {
        super(context, 0, machineList);
        mContext = context;
        mMachineList = machineList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.drop_down_list, parent, false);
        }
        Machine machine = mMachineList.get(position);
        TextView textView = view.findViewById(R.id.textview_bottom);
        textView.setText(machine.getName());
        TextView textView1 = view.findViewById(R.id.textview_bottom_small);
        textView1.setText(String.valueOf(machine.getId()));
        return view;
    }

}
