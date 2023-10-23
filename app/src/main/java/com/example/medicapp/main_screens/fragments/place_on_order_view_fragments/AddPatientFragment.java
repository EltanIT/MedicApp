package com.example.medicapp.main_screens.fragments.place_on_order_view_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.medicapp.R;
import com.example.medicapp.databinding.FragmentAddPatientBinding;
import com.example.medicapp.main_screens.objects.Order;
import com.example.medicapp.main_screens.objects.Profile;

import java.util.ArrayList;
import java.util.List;

public class AddPatientFragment extends Fragment {
        private FragmentAddPatientBinding binding;
        private AddPatientListener addPatientListener;
        private Profile selectProfile;

        public AddPatientFragment(AddPatientListener addPatientListener){
            this.addPatientListener = addPatientListener;
        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPatientBinding.inflate(inflater,container,false);
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBinding();
                setting();
            }
        });
        openView();
        return binding.getRoot();
    }

    private void setting() {
        binding.profileContinueButton.setEnabled(false);
    }

    private void setBinding() {
        binding.patient1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.patient1View.setSelected(true);
                binding.name1.setTextColor(getResources().getColor(R.color.white));
                binding.patient2View.setSelected(false);
                binding.name2.setTextColor(getResources().getColor(R.color.black));

                binding.profileContinueButton.setEnabled(true);
            }
        });
        binding.patient2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.patient2View.setSelected(true);
                binding.name2.setTextColor(getResources().getColor(R.color.white));
                binding.patient1View.setSelected(false);
                binding.name1.setTextColor(getResources().getColor(R.color.black));

                binding.profileContinueButton.setEnabled(true);
            }
        });

        binding.profileContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.patient2View.setSelected(false);
                if(binding.patient1View.isSelected()){
                    selectProfile = new Profile("Эдуард", "Константинович", "08.05.2001", "Тицкий", (byte) 0);
                }
                else{
                    selectProfile = new Profile("Елена", "Олеговна", "05.11.1995", "Тицкая", (byte) 1);
                }
                addPatientListener.ButtonClick(selectProfile);
                closeView();
            }
        });
        binding.closeProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeView();
            }
        });
        binding.profileClView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeView();
            }
        });
        binding.profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void openView(){
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_mainscreen_open_datadialog);
        binding.profileCardView.startAnimation(animation);
    }

    private void closeView(){
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_mainscreen_close_datadialog);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.profileCardView.startAnimation(animation);
    }
}