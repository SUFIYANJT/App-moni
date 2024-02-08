package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private BottomSheetFragment bottomSheetFragment;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchView = findViewById(R.id.search_view);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        fab = findViewById(R.id.fab);


        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentAdapter.addFragment(new ExistingActivity(), "Existing Activity");
        fragmentAdapter.addFragment(new IssuedActivity(), "Issued Activity");
        fragmentAdapter.addFragment(new PendingActivity(), "Pending Activity");
        fragmentAdapter.addFragment(new InspectorReview(), "Inspector Review");
        viewPager.setAdapter(fragmentAdapter);

        tabLayout.setupWithViewPager(viewPager);

        bottomSheetFragment = new BottomSheetFragment();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetFragment.isVisible()) {
                    bottomSheetFragment.dismiss();
                } else {
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not needed for this example
            }




            @Override
            public void onPageSelected(int position) {
                // Check the position of the selected fragment and update FAB visibility
                if (position == 3) { // Assuming InspectorReview is at position 3
                    fab.setVisibility(View.INVISIBLE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                // Not needed for this example
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Show keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
            }
        });
    }

        // Set a listener for the search view

    }
