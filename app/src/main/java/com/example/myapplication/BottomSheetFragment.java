package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.network.WebSocketClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private TextInputLayout activityNameInputLayout;
    private TextInputLayout activityDescriptionInputLayout;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView1;
    private AutoCompleteTextView autoCompleteTextView2;
    private Button confirmButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        activityNameInputLayout = view.findViewById(R.id.Acivity_name);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView1 = view.findViewById(R.id.autoCompleteTextView1);
        autoCompleteTextView2 = view.findViewById(R.id.autoCompleteTextView2);
        confirmButton = view.findViewById(R.id.confirmButton);

        // Create an ArrayAdapter with the elements "1", "2", "3", and "4"
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                new String[]{"machine1", "machine2", "machine3", "machine4"});
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                new String[]{"Component1", "Component2", "Component3", "Component4"});
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                new String[]{"Schedule1", "Schedule2", "Schedule3", "Schedule4"});
        autoCompleteTextView.setAdapter(adapter1);
        autoCompleteTextView1.setAdapter(adapter2);
        autoCompleteTextView2.setAdapter(adapter3);
        confirmButton.setOnClickListener(view1 -> confirmInput());
        activityDescriptionInputLayout=view.findViewById(R.id.Description);
        return view;
    }

    private void confirmInput() {
        // Validate input, for example:
        if (activityNameInputLayout.getEditText().getText().toString().isEmpty()) {
            activityNameInputLayout.setError("Activity name cannot be empty");
            return;
        } else if (activityDescriptionInputLayout.getEditText().getText().toString().isEmpty()) {
            activityDescriptionInputLayout.setError("Insert the Description/Procedure");
        } else {
            activityNameInputLayout.setError(null);
        }
        Toast.makeText(requireContext(), "Activity created" ,Toast.LENGTH_SHORT).show();
    }
}
