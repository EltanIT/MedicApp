package com.example.medicapp.login_screens.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicapp.R;
import com.example.medicapp.main_screens.MainScreen;
import com.example.medicapp.main_screens.objects.Profile;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateCardFragment extends Fragment {

    private Button continue_button;
    private EditText et_name, et_otchestvo, et_lastname, et_birthday;
    private TextView miss_fragment;
    private Spinner sex_s;
    private byte sex;
    private List<String> sexList = new ArrayList<>();
    private SharedPreferences preferences;
    private SharedPreferences preferencesToken;
    private Profile profile;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_card, container, false);
        preferences = getActivity().getSharedPreferences("Table", Context.MODE_PRIVATE);
        preferencesToken = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
        elementIdentification(view);
        getToken();
        addList();
        setListeners();

        continue_button.setEnabled(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sexList);
        sex_s.setAdapter(adapter);

        return view;
    }

    private void setListeners() {
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProfile();
                saveProfile(profile);
            }
        });

        miss_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                startActivity(intent);
            }
        });

        sex_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sex_s.getItemAtPosition(i).equals("Пол")){
                    sex = (byte) (i-1);
                }
                checkContent();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.length() == 0)){
                    et_name.setActivated(true);
                    et_name.setTextColor(getResources().getColor(R.color.black));

                }
                else {
                    et_name.setActivated(false);
                    et_name.setTextColor(getResources().getColor(R.color.Caption));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkContent();
            }
        });
        et_otchestvo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.length() ==0)){
                    et_otchestvo.setActivated(true);
                    et_otchestvo.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    et_otchestvo.setActivated(false);
                    et_otchestvo.setTextColor(getResources().getColor(R.color.black));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkContent();
            }
        });
        et_lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.length() ==0)){
                    et_lastname.setActivated(true);
                    et_lastname.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    et_lastname.setActivated(false);
                    et_lastname.setTextColor(getResources().getColor(R.color.black));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkContent();
            }
        });
        et_birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.length() ==0)){
                    et_birthday.setActivated(true);
                    et_birthday.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    et_birthday.setActivated(false);
                    et_birthday.setTextColor(getResources().getColor(R.color.black));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkContent();
            }
        });


    }
    private void addList() {
        sexList.add("Пол");
        sexList.add("Мужской");
        sexList.add("Женский");
    }
    private void checkContent(){
        String name = String.valueOf(et_name.getText());
        String otchestvo = String.valueOf(et_otchestvo.getText());
        String lastname = String.valueOf(et_lastname.getText());
        String birthday = String.valueOf(et_birthday.getText());
        String sex = sex_s.getSelectedItem().toString();

        if (!name.isEmpty() && !otchestvo.isEmpty() && !lastname.isEmpty() && !birthday.isEmpty() && !(sex_s.getSelectedItemPosition() == 0)){
            continue_button.setEnabled(true);
        }
        else
            continue_button.setEnabled(false);
    }
    private void getProfile(){
        String name = String.valueOf(et_name.getText());
        String patronymic = String.valueOf(et_otchestvo.getText());
        String lastname = String.valueOf(et_lastname.getText());
        String birthday = String.valueOf(et_birthday.getText());
        sex = (byte) sex_s.getSelectedItemPosition();
        profile = new Profile(name,patronymic,lastname,birthday,sex);
    }
    private void saveProfile(Profile profile){
        continue_button.setClickable(false);
        Gson gson = new Gson();
        String profileJson = gson.toJson(profile, Profile.class);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("profileJson");
        editor.putString("profileJson", profileJson);
        editor.commit();
        new SaveProfile().execute(profileJson);
    }
    void elementIdentification(View view){
        continue_button = view.findViewById(R.id.continue_button);
        continue_button.setSelected(false);

        et_name = view.findViewById(R.id.et_name);
        et_otchestvo = view.findViewById(R.id.et_otchestvo);
        et_lastname = view.findViewById(R.id.et_lastname);
        et_birthday = view.findViewById(R.id.et_birthday);
        miss_fragment = view.findViewById(R.id.miss_fragment);

        sex_s = view.findViewById(R.id.sex_s);

    }

    private void getToken(){
        token = preferencesToken.getString("token", null);
    }

    private class SaveProfile extends AsyncTask<String, Void, String>{
        final private String POST_PROFILE_URL = "http://192.168.144.66:8080/api/profile";

        @Override
        protected String doInBackground(String... strings) {
            if (token == null){
                return null;
            }
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(2000, TimeUnit.MILLISECONDS)
                    .build();;
            Gson gson = new Gson();
            Map<String, Object> params = new HashMap<>();
            params.put("firstName", profile.getName());
            params.put("lastName", profile.getSurname());
            params.put("patronymic", profile.getPatronymic());
            params.put("dob", profile.getDob());
            params.put("gender", Integer.parseInt(String.valueOf(profile.getSex())));

            String body = gson.toJson(params);

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);

            Request request = new Request.Builder().url(POST_PROFILE_URL)
                    .addHeader("Authorization", token)
                    .post(requestBody)
                    .build();

            try(Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()){
                    if (response.code() == 409){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Профиль уже существует", Toast.LENGTH_LONG).show();
                            }
                        });
                        return "";
                    }
                    else if (response.code() == 401){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Ошибка авторизации", Toast.LENGTH_LONG).show();
                                continue_button.setClickable(true);
                            }
                        });
                        return "";
                    }
                    return null;
                }
                else {
                    if (response.code() == 200){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Профиль успешно создан", Toast.LENGTH_LONG).show();
                            }
                        });
                        Intent intent = new Intent(getActivity(), MainScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            } catch (IOException e) {

               return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s==null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Ошибка соединения с сервером", Toast.LENGTH_LONG).show();
                        continue_button.setClickable(true);
                    }
                });
            }
            else if (!s.equals("")){
                continue_button.setClickable(true);
            }
        }
    }
}