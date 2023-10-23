package com.example.medicapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.medicapp.hello_screen.fragments.Hello1;
import com.example.medicapp.hello_screen.fragments.Hello2;
import com.example.medicapp.hello_screen.fragments.Hello3;
import com.example.medicapp.hello_screen.fragments.hsAdapterFragments;
import com.google.android.material.tabs.TabLayout;
public class MainActivity extends AppCompatActivity {
    ViewPager viewpager;
    TabLayout tabLayout;
    ConstraintLayout sign_in_fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager = findViewById(R.id.viewpager);
        sign_in_fragments = findViewById(R.id.sign_in_fragments);
        tabLayout = findViewById(R.id.tabLayout);

        hsAdapterFragments hsAdapterFragments = new hsAdapterFragments(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        hsAdapterFragments.addFragment(new Hello1());
        hsAdapterFragments.addFragment(new Hello2());
        hsAdapterFragments.addFragment(new Hello3());

        viewpager.setAdapter(hsAdapterFragments);
        tabLayout.setupWithViewPager(viewpager,true);
    }
}