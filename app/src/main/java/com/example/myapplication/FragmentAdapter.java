package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragmentArrayLis=new ArrayList<>();
    private final ArrayList<String> fragmentTitles=new ArrayList<>();
    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayLis.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayLis.size();
    }
    public void addFragment(Fragment fragment,String title){
        fragmentArrayLis.add(fragment);
        fragmentTitles.add(title);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}
