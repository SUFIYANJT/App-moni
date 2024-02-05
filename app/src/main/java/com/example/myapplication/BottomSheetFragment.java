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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private TextInputLayout activityNameInputLayout;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line,
                new String[]{"1", "2", "3", "4"});

        // Set the ArrayAdapter to each AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView1.setAdapter(adapter);
        autoCompleteTextView2.setAdapter(adapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput();
            }
        });

        return view;
    }

    private void confirmInput() {
        // Validate input, for example:
        if (activityNameInputLayout.getEditText().getText().toString().isEmpty()) {
            activityNameInputLayout.setError("Activity name cannot be empty");
            return;
        } else {
            activityNameInputLayout.setError(null);
        }

        // Perform other input validations as needed

        // Retrieve selected items from AutoCompleteTextViews
        // You can now use the selected items as needed

        // Show a confirmation message or perform other actions
        Toast.makeText(requireContext(), "Activity created" ,Toast.LENGTH_SHORT).show();
    }
}
