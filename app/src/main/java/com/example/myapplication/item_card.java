package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class item_card extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Find the CardView in the item view
        // Assuming you have a FloatingActionButton
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBottomSheetFragmentExixting bottomSheetFragment = new MyBottomSheetFragmentExixting();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

            }
        });

    }

}
