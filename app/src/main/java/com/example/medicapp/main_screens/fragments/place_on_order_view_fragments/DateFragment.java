package com.example.medicapp.main_screens.fragments.place_on_order_view_fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.medicapp.R;
import com.example.medicapp.databinding.FragmentDateBinding;
import com.example.medicapp.main_screens.adapters.OrderDateSpinnerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateFragment extends Fragment {
    private DateButtonListener dateButtonListener;
    private FragmentDateBinding binding;
    private List<String> dateList = new ArrayList<>();
    private int dateHour;

    public DateFragment( DateButtonListener dateButtonListener){
        this.dateButtonListener = dateButtonListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDateBinding.inflate(inflater, container, false);
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setting();
                setBinding();
                setBindingDate();
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null){
            dateHour = bundle.getInt("date", 0);
            if (dateHour != 0){
                if (dateHour == 10){
                    binding.time10.setSelected(true);
                    binding.time10.setTextColor(getResources().getColor(R.color.white));
                    binding.dateContinueButton.setEnabled(true);
                }
                else if(dateHour == 13){
                    binding.time13.setSelected(true);
                    binding.time13.setTextColor(getResources().getColor(R.color.white));
                    binding.dateContinueButton.setEnabled(true);
                }
                else if(dateHour == 14){
                    binding.time14.setSelected(true);
                    binding.time14.setTextColor(getResources().getColor(R.color.white));
                    binding.dateContinueButton.setEnabled(true);
                }
                else if(dateHour == 15){
                    binding.time15.setSelected(true);
                    binding.time15.setTextColor(getResources().getColor(R.color.white));
                    binding.dateContinueButton.setEnabled(true);
                }
                else if(dateHour == 16){
                    binding.time16.setSelected(true);
                    binding.time16.setTextColor(getResources().getColor(R.color.white));
                    binding.dateContinueButton.setEnabled(true);
                }
                else if(dateHour == 18){
                    binding.time18.setSelected(true);
                    binding.time18.setTextColor(getResources().getColor(R.color.white));
                    binding.dateContinueButton.setEnabled(true);
                }
                else if(dateHour == 19){
                    binding.time19.setSelected(true);
                    binding.time19.setTextColor(getResources().getColor(R.color.white));
                    binding.dateContinueButton.setEnabled(true);
                }
            }
        }

        openView();
        return binding.getRoot();
    }

    private void setting() {
        binding.dateContinueButton.setEnabled(false);

        Calendar calendar = Calendar.getInstance();
        dateList.add("сегодня, "+calendar.get(Calendar.DAY_OF_MONTH)+" сентября");

        OrderDateSpinnerAdapter dateSpinnerAdapter = new OrderDateSpinnerAdapter(getContext(), R.layout.view_place_on_order_select_date, dateList);
        binding.dateSp.setAdapter(dateSpinnerAdapter);
    }

    private void setBinding() {
        binding.dateContinueButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                LocalDateTime ldt = LocalDateTime.now();

                if (binding.time10.isSelected()){
                    ldt = LocalDateTime.now().withHour(10);
                } else if (binding.time13.isSelected()){
                    ldt = LocalDateTime.now().withHour(13);
                }
                else if (binding.time14.isSelected()){
                    ldt = LocalDateTime.now().withHour(14);
                }
                else if (binding.time15.isSelected()){
                    ldt = LocalDateTime.now().withHour(15);
                }
                else if (binding.time16.isSelected()){
                    ldt = LocalDateTime.now().withHour(16);
                }
                else if (binding.time18.isSelected()){
                    ldt = LocalDateTime.now().withHour(18);
                }
                else if (binding.time19.isSelected()){
                    ldt = LocalDateTime.now().withHour(19);
                }
                dateButtonListener.ButtonClick(true, ldt);

                closeView();

            }
        });
        binding.closeDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeView();
            }
        });
        binding.dateClView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeView();
            }
        });
        binding.dateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setBindingDate() {
        binding.time10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.time10.setTextColor(getResources().getColor(R.color.white));
                binding.time13.setTextColor(getResources().getColor(R.color.Caption));
                binding.time14.setTextColor(getResources().getColor(R.color.Caption));
                binding.time15.setTextColor(getResources().getColor(R.color.Caption));
                binding.time16.setTextColor(getResources().getColor(R.color.Caption));
                binding.time18.setTextColor(getResources().getColor(R.color.Caption));
                binding.time19.setTextColor(getResources().getColor(R.color.Caption));

                binding.time10.setSelected(true);
                binding.time13.setSelected(false);
                binding.time14.setSelected(false);
                binding.time15.setSelected(false);
                binding.time16.setSelected(false);
                binding.time18.setSelected(false);
                binding.time19.setSelected(false);

                binding.dateContinueButton.setEnabled(true);
            }
        });
        binding.time13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.time10.setTextColor(getResources().getColor(R.color.Caption));
                binding.time13.setTextColor(getResources().getColor(R.color.white));
                binding.time14.setTextColor(getResources().getColor(R.color.Caption));
                binding.time15.setTextColor(getResources().getColor(R.color.Caption));
                binding.time16.setTextColor(getResources().getColor(R.color.Caption));
                binding.time18.setTextColor(getResources().getColor(R.color.Caption));
                binding.time19.setTextColor(getResources().getColor(R.color.Caption));

                binding.time10.setSelected(false);
                binding.time13.setSelected(true);
                binding.time14.setSelected(false);
                binding.time15.setSelected(false);
                binding.time16.setSelected(false);
                binding.time18.setSelected(false);
                binding.time19.setSelected(false);

                binding.dateContinueButton.setEnabled(true);
            }
        });
        binding.time14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.time10.setTextColor(getResources().getColor(R.color.Caption));
                binding.time13.setTextColor(getResources().getColor(R.color.Caption));
                binding.time14.setTextColor(getResources().getColor(R.color.white));
                binding.time15.setTextColor(getResources().getColor(R.color.Caption));
                binding.time16.setTextColor(getResources().getColor(R.color.Caption));
                binding.time18.setTextColor(getResources().getColor(R.color.Caption));
                binding.time19.setTextColor(getResources().getColor(R.color.Caption));

                binding.time10.setSelected(false);
                binding.time13.setSelected(false);
                binding.time14.setSelected(true);
                binding.time15.setSelected(false);
                binding.time16.setSelected(false);
                binding.time18.setSelected(false);
                binding.time19.setSelected(false);

                binding.dateContinueButton.setEnabled(true);
            }
        });
        binding.time15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.time10.setTextColor(getResources().getColor(R.color.Caption));
                binding.time13.setTextColor(getResources().getColor(R.color.Caption));
                binding.time14.setTextColor(getResources().getColor(R.color.Caption));
                binding.time15.setTextColor(getResources().getColor(R.color.white));
                binding.time16.setTextColor(getResources().getColor(R.color.Caption));
                binding.time18.setTextColor(getResources().getColor(R.color.Caption));
                binding.time19.setTextColor(getResources().getColor(R.color.Caption));

                binding.time10.setSelected(false);
                binding.time13.setSelected(false);
                binding.time14.setSelected(false);
                binding.time15.setSelected(true);
                binding.time16.setSelected(false);
                binding.time18.setSelected(false);
                binding.time19.setSelected(false);

                binding.dateContinueButton.setEnabled(true);
            }
        });
        binding.time16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.time10.setTextColor(getResources().getColor(R.color.Caption));
                binding.time13.setTextColor(getResources().getColor(R.color.Caption));
                binding.time14.setTextColor(getResources().getColor(R.color.Caption));
                binding.time15.setTextColor(getResources().getColor(R.color.Caption));
                binding.time16.setTextColor(getResources().getColor(R.color.white));
                binding.time18.setTextColor(getResources().getColor(R.color.Caption));
                binding.time19.setTextColor(getResources().getColor(R.color.Caption));

                binding.time10.setSelected(false);
                binding.time13.setSelected(false);
                binding.time14.setSelected(false);
                binding.time15.setSelected(false);
                binding.time16.setSelected(true);
                binding.time18.setSelected(false);
                binding.time19.setSelected(false);

                binding.dateContinueButton.setEnabled(true);
            }
        });
        binding.time18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.time10.setTextColor(getResources().getColor(R.color.Caption));
                binding.time13.setTextColor(getResources().getColor(R.color.Caption));
                binding.time14.setTextColor(getResources().getColor(R.color.Caption));
                binding.time15.setTextColor(getResources().getColor(R.color.Caption));
                binding.time16.setTextColor(getResources().getColor(R.color.Caption));
                binding.time18.setTextColor(getResources().getColor(R.color.white));
                binding.time19.setTextColor(getResources().getColor(R.color.Caption));

                binding.time10.setSelected(false);
                binding.time13.setSelected(false);
                binding.time14.setSelected(false);
                binding.time15.setSelected(false);
                binding.time16.setSelected(false);
                binding.time18.setSelected(true);
                binding.time19.setSelected(false);

                binding.dateContinueButton.setEnabled(true);
            }
        });
        binding.time19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.time10.setTextColor(getResources().getColor(R.color.Caption));
                binding.time13.setTextColor(getResources().getColor(R.color.Caption));
                binding.time14.setTextColor(getResources().getColor(R.color.Caption));
                binding.time15.setTextColor(getResources().getColor(R.color.Caption));
                binding.time16.setTextColor(getResources().getColor(R.color.Caption));
                binding.time18.setTextColor(getResources().getColor(R.color.Caption));
                binding.time19.setTextColor(getResources().getColor(R.color.white));

                binding.time10.setSelected(false);
                binding.time13.setSelected(false);
                binding.time14.setSelected(false);
                binding.time15.setSelected(false);
                binding.time16.setSelected(false);
                binding.time18.setSelected(false);
                binding.time19.setSelected(true);

                binding.dateContinueButton.setEnabled(true);
            }
        });
    }

    private void openView(){
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_mainscreen_open_datadialog);
        binding.dateCardView.startAnimation(animation);
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
        binding.dateCardView.startAnimation(animation);
    }
}