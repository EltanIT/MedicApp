package com.example.medicapp.login_screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.medicapp.R;
import com.example.medicapp.login_screens.fragments.SignInFragment;
import com.example.medicapp.main_screens.MainScreen;

public class Login_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        settingFragments(new SignInFragment());
//        startActivity(new Intent(getApplicationContext(), MainScreen.class));

    }

    void settingFragments(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.login_screen_layout_cl, fragment).commit();

    }
}