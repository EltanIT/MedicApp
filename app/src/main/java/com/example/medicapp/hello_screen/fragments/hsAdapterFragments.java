package com.example.medicapp.hello_screen.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class hsAdapterFragments extends FragmentPagerAdapter{

    List<Fragment> pages = new ArrayList<>();

    public hsAdapterFragments(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    public void addFragment(Fragment fragment){
        pages.add(fragment);
    }
}

