package com.example.myapplication;

import static android.widget.Toast.makeText;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.button.MaterialButton;
 // Import MaterialButton

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private BottomSheetFragment bottomSheetFragment;
    private SearchView searchView;
    private SearchBar searchBar;
    private MaterialButton menuButton; // Define MaterialButton for the menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search_view);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        fab = findViewById(R.id.fab);
       // Initialize menu button

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
                if (position == 3) {
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


        // Set click listener for menu button
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView1=(SearchView) findViewById(R.id.search_View);
        MenuItem.OnActionExpandListener onActionExpandListener=new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
                makeText(MainActivity.this, "fkwefiw", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
                return true;
            }
        };
        menu.findItem(R.id.search_View).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView2=(SearchView) menu.findItem(R.id.search_View).getActionView();
        searchView2.setOnKeyListener((v, keyCode, event) -> {
            Log.d(TAG, "onKey: "+event.getCharacters());
            return false;
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void showMenu(View view) {

        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);

        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override

            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menu_settings) {

                    Toast.makeText(MainActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();

                    return true;

                } else if (item.getItemId() == R.id.menu_logout) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();

                    return true;

                }else if(item.getItemId()==R.id.search_view){
                    SearchView searchView1=(SearchView) item.getActionView();

                }

                return false;

            }

        });

        popupMenu.show();

    }
}
