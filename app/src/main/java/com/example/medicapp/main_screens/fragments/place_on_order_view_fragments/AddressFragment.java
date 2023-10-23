package com.example.medicapp.main_screens.fragments.place_on_order_view_fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.example.medicapp.R;
import com.example.medicapp.databinding.FragmentAddressBinding;
import com.example.medicapp.main_screens.objects.Order;

public class AddressFragment extends Fragment{
    private FragmentAddressBinding binding;
    private Order.ClientAddress address;
    private boolean checkAddress;
    AddressButtonListener addressButtonListener;

    public AddressFragment(AddressButtonListener addressButtonListener){
        this.addressButtonListener = addressButtonListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(inflater, container, false);
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setting();
                setBinding();
                setBindingAddressEt();
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null){
            address = bundle.getParcelable("address");
            if (address != null){
                binding.addressEt.setText(address.getAddress());
                binding.entranceEt.setText(address.getEntrance()+"");
                binding.flatEt.setText(address.getFlat()+"");
                binding.longitudeEt.setText(address.getLongitude()+"");
                binding.floorEt.setText(address.getFloor()+"");
                binding.heightEt.setText(address.getHeight()+"");
                binding.widthEt.setText(address.getWidth()+"");
                binding.intercomEt.setText(address.getIntercom()+"");

                if (address.getBuildingType() == 1){
                    binding.saveSwitch.setChecked(true);
                }


                checkAddressEt();
            }
        }

        openView();
        return binding.getRoot();
    }

    private void setting() {
        binding.addressContinueButton.setEnabled(false);
    }
    private void setBinding() {
        binding.addressCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.addressBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAddress){
                    int check = 0;
                    if (binding.saveSwitch.isChecked()){
                        check = 1;
                    }
                    address = new Order.ClientAddress(String.valueOf(binding.addressEt.getText()),
                            Integer.parseInt(String.valueOf(binding.longitudeEt.getText())),
                            Integer.parseInt(String.valueOf(binding.widthEt.getText())),
                            Integer.parseInt(String.valueOf(binding.heightEt.getText())),
                            Integer.parseInt(String.valueOf(binding.flatEt.getText())),
                            Integer.parseInt(String.valueOf(binding.entranceEt.getText())),
                            Integer.parseInt(String.valueOf(binding.floorEt.getText())),
                            String.valueOf(binding.intercomEt.getText()),
                            check);

                    addressButtonListener.ButtonClick(false, address);
                }
                closeView();

            }
        });
        binding.closeAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkAddress){
                    int check = 0;
                    if (binding.saveSwitch.isChecked()){
                        check = 1;
                    }
                    address = new Order.ClientAddress(String.valueOf(binding.addressEt.getText()),
                            Integer.parseInt(String.valueOf(binding.longitudeEt.getText())),
                            Integer.parseInt(String.valueOf(binding.widthEt.getText())),
                            Integer.parseInt(String.valueOf(binding.heightEt.getText())),
                            Integer.parseInt(String.valueOf(binding.flatEt.getText())),
                            Integer.parseInt(String.valueOf(binding.entranceEt.getText())),
                            Integer.parseInt(String.valueOf(binding.floorEt.getText())),
                            String.valueOf(binding.intercomEt.getText()),
                            check);

                    addressButtonListener.ButtonClick(false, address);
                }
                closeView();

            }
        });

        binding.addressContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check = 0;
                if (binding.saveSwitch.isChecked()){
                    check = 1;
                }

                address = new Order.ClientAddress(String.valueOf(binding.addressEt.getText()),
                        Integer.parseInt(String.valueOf(binding.longitudeEt.getText())),
                        Integer.parseInt(String.valueOf(binding.widthEt.getText())),
                        Integer.parseInt(String.valueOf(binding.heightEt.getText())),
                        Integer.parseInt(String.valueOf(binding.flatEt.getText())),
                        Integer.parseInt(String.valueOf(binding.entranceEt.getText())),
                        Integer.parseInt(String.valueOf(binding.floorEt.getText())),
                        String.valueOf(binding.intercomEt.getText()),
                        check);

                addressButtonListener.ButtonClick(true, address);
                closeView();
            }
        });
    }
    private void setBindingAddressEt(){
        binding.addressEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAddressEt();
            }
        });
        binding.longitudeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAddressEt();
            }
        });
        binding.heightEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAddressEt();
            }
        });
        binding.widthEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAddressEt();
            }
        });
        binding.flatEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAddressEt();
            }
        });
        binding.floorEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAddressEt();
            }
        });
        binding.entranceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAddressEt();
            }
        });
        binding.intercomEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAddressEt();
            }
        });
    }
    private void checkAddressEt(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!String.valueOf(binding.intercomEt.getText()).equals("") && !String.valueOf(binding.floorEt.getText()).equals("") && !String.valueOf(binding.entranceEt.getText()).equals("") && !String.valueOf(binding.flatEt.getText()).equals("") && !String.valueOf(binding.heightEt.getText()).equals("") && !String.valueOf(binding.widthEt.getText()).equals("") && !String.valueOf(binding.addressEt.getText()).equals("") && !String.valueOf(binding.longitudeEt.getText()).equals("")){
                    checkAddress = true;
                }
                else checkAddress = false;
                binding.addressContinueButton.setEnabled(checkAddress);
            }
        });


    }



    private void openView(){
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_mainscreen_open_datadialog);
        binding.addressCardView.startAnimation(animation);
    }

    private void closeView(){
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_mainscreen_close_datadialog);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                requireActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.addressCardView.startAnimation(animation);
    }
}