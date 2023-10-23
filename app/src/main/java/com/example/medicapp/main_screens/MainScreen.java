package com.example.medicapp.main_screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.medicapp.R;
import com.example.medicapp.databinding.ActivityMainScreenBinding;
import com.example.medicapp.main_screens.fragments.AnalysisFragment;
import com.example.medicapp.main_screens.fragments.ProfileFragment;

public class MainScreen extends AppCompatActivity {
    public interface BackpressedListener{
        boolean onBackPressed();
    }
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.most_mainscreen_view);
        Fragment fragmentAnalysisScreen = getSupportFragmentManager().findFragmentById(R.id.mainscreen_view);

        if (fragment!= null){
            if(fragment instanceof BackpressedListener){
                if (!(((BackpressedListener) fragment).onBackPressed())){
                    super.onBackPressed();
                }
            }
            else {
                super.onBackPressed();
            }
        }
        else if (fragmentAnalysisScreen!= null){
            if (fragmentAnalysisScreen instanceof BackpressedListener){
                if (!(((BackpressedListener)fragmentAnalysisScreen).onBackPressed())){
                    super.onBackPressed();
                }
            }
            else {
                super.onBackPressed();
            }
        }
    }

    ActivityMainScreenBinding binding;
    private AnalysisFragment analysisFragment = new AnalysisFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private Fragment activeFragment = analysisFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new Thread(this::elementSetting).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                settingFragments();
                selectFragment(analysisFragment);
            }
        }).start();

    }
    private void elementSetting(){

       binding.navView.setOnItemSelectedListener(item ->  {
            int id = item.getItemId();
            if(id == R.id.analisis){
                selectFragment(analysisFragment);
                activeFragment = analysisFragment;
                return true;
            }
            else if(id == R.id.profile) {
                selectFragment(profileFragment);
                activeFragment = profileFragment;
                return true;
            }
            return false;
        });
    }
    private void settingFragments(){
        fragmentManager.beginTransaction()
                .add(R.id.mainscreen_view, profileFragment, "profile").hide(profileFragment)
                .add(R.id.mainscreen_view, analysisFragment, "analysis").hide(analysisFragment)
                .commit();
    }
    private void selectFragment(Fragment fragment){
        fragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit();
    }

    public void closeBottomNavigation(int i){
        binding.cardView.setVisibility(i);
    }
}