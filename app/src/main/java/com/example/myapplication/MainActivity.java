package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private BottomSheetFragment bottomSheetFragment;
    private MaterialButton menuButton;
    private static final String TAG = "MainActivity"; // Or any appropriate tag name


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        fab = findViewById(R.id.fab);

        // Set up fragments
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);


        // Set up search functionality

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchItem = menu.findItem(R.id.search_view);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        // Set query text listener

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override

            public boolean onQueryTextSubmit(String query) {

                // Handle search query submission

                updateFragmentSearchQuery(query);

                return false;

            }


            @Override

            public boolean onQueryTextChange(String newText) {

                // Pass the newText to your fragments to update the UI accordingly

                updateFragmentSearchQuery(newText);

                return true;

            }

        });


        return true;

    }


    private void updateFragmentSearchQuery(String query) {

        // Update each fragment with the new search query

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {

            if (fragment instanceof SearchableFragment) {

                ((SearchableFragment) fragment).updateSearchQuery(query);

            }

        }

    }


    // ...

}
    // Other methods as before...

