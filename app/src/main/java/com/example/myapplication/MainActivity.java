package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
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
        MaterialToolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("tranvancore Cements");
        setSupportActionBar(toolbar);
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
        // Inflate menu with items using MenuInflator
        Log.d(TAG, "onCreateOptionsMenu: called this function");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search_View);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        // attach setOnQueryTextListener
        // to search view defined above
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                // Override onQueryTextSubmit method which is call when submit query is searched
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // If the list contains the search query than filter the adapter
                    // using the filter method with the query as its argument
                    Toast.makeText(MainActivity.this, "changing", Toast.LENGTH_SHORT).show();
                    updateFragmentSearchQuery(query);
                    return true;
                }

                // This method is overridden to filter the adapter according
                // to a search query when the user is typing search
                @Override
                public boolean onQueryTextChange(String newText) {
                    updateFragmentSearchQuery(newText);
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Handle item selection
        if (id == R.id.search_View) {
            // Handle Item 1 selection
            return true;
        } else if (id == R.id.menu_settings) {
            // Handle Item 2 selection
            return true;
        } else if (id == R.id.menu_logout) {
            // Handle Item 3 selection
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

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

