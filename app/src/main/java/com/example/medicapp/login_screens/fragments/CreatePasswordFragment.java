package com.example.medicapp.login_screens.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicapp.R;

import java.util.Objects;

public class CreatePasswordFragment extends Fragment {
    private View delete_button;
    private View view1,view2,view3,view4;
//    private EditText pinCode_et;
    private Button btn_0, btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9;
    private TextView miss_fragment;
    private String pincode = null;
    private SharedPreferences preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_password, container, false);
        elementIdentification(view);

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pincode != null && pincode.length() > 0) {

                    pincode = pincode.substring(0, pincode.length() - 1);
                    redactViewDelete();
                }

            }
        });

        miss_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_screen_layout_cl, new CreateCardFragment()).addToBackStack("createCard")
                        .commit();
            }
        });

//        pinCode_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String text = String.valueOf(pinCode_et.getText());
//
//                if (text.length()==4){
//                        savePassword(text);
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.login_screen_layout_cl, new CreateCardFragment()).addToBackStack("createCard")
//                                .commit();
//                }
//            }
//        });

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    pinCode_et.setText(pinCode_et.getText()+"0");
                    pincode += "0";
                    redactViewPin();
            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pinCode_et.setText(pinCode_et.getText()+"1");
                pincode += "1";
                redactViewPin();
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pinCode_et.setText(pinCode_et.getText()+"2");
                pincode += "2";
                redactViewPin();
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pinCode_et.setText(pinCode_et.getText()+"3");
                pincode += "3";
                redactViewPin();
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pinCode_et.setText(pinCode_et.getText()+"4");
                pincode += "4";
                redactViewPin();
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pinCode_et.setText(pinCode_et.getText()+"5");
                pincode += "5";
                redactViewPin();
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pinCode_et.setText(pinCode_et.getText()+"6");
                pincode += "6";
                redactViewPin();
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pinCode_et.setText(pinCode_et.getText()+"7");
                pincode += "7";
                redactViewPin();
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pinCode_et.setText(pinCode_et.getText()+"8");
                pincode += "8";
                redactViewPin();
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pinCode_et.setText(pinCode_et.getText()+"9");
                pincode += "9";
                redactViewPin();
            }
        });
        return view;
    }

    private void redactViewDelete() {
        if (view1.isSelected()){
            if (view2.isSelected()){
                if (view3.isSelected()){
                    if (view4.isSelected()){
                        view4.setSelected(false);
                    }
                    else view3.setSelected(false);
                }
                else view2.setSelected(false);
            }
            else view1.setSelected(false);
        }
    }

    private void redactViewPin() {
        if (view1.isSelected()){
            if (view2.isSelected()){
                if (view3.isSelected()){
                    view4.setSelected(true);
                    savePassword(pincode);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.login_screen_layout_cl, new CreateCardFragment()).addToBackStack("createCard")
                            .commit();
                }
                else {
                    view3.setSelected(true);
                }
            }
            else {
                view2.setSelected(true);
            }
        }
        else {
            view1.setSelected(true);
        }
    }


    private void savePassword(String text){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("password");
        editor.commit();
        editor.putString("password", text);
        editor.commit();
    }

    private void getPassword(){
       pincode = preferences.getString("password", null);
        if (pincode == null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_screen_layout_cl, new CreateCardFragment()).addToBackStack("createCard")
                    .commit();
        }
    }

     void elementIdentification(View view){
        delete_button = view.findViewById(R.id.delete_button);
//        pinCode_et = view.findViewById(R.id.pinCode_et);
        miss_fragment = view.findViewById(R.id.miss_fragment);
        preferences = getActivity().getSharedPreferences("Table",Context.MODE_PRIVATE);

        btn_0 = view.findViewById(R.id.btn_0);
        btn_1 = view.findViewById(R.id.btn_1);
        btn_2 = view.findViewById(R.id.btn_2);
        btn_3 = view.findViewById(R.id.btn_3);
        btn_4 = view.findViewById(R.id.btn_4);
        btn_5 = view.findViewById(R.id.btn_5);
        btn_6 = view.findViewById(R.id.btn_6);
        btn_7 = view.findViewById(R.id.btn_7);
        btn_8 = view.findViewById(R.id.btn_8);
        btn_9 = view.findViewById(R.id.btn_9);

        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        view4 = view.findViewById(R.id.view4);
    }
}