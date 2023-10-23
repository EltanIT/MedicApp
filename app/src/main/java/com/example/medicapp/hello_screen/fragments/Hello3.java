package com.example.medicapp.hello_screen.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.medicapp.R;
import com.example.medicapp.login_screens.Login_screen;
import com.google.android.material.tabs.TabLayout;


public class Hello3 extends Fragment {

    TabLayout tabLayout;
    TextView complete_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitoring,container,false);
        tabLayout = view.findViewById(R.id.tabLayout);
        complete_button = view.findViewById(R.id.complete_button);
        complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(view.getContext(), Login_screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

//        tabLayout.setupWithViewPager(getActivity().findViewById(R.id.viewpager));
        return view;
    }
}