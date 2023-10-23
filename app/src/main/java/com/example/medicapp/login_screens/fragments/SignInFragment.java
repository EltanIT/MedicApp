package com.example.medicapp.login_screens.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.CalendarContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicapp.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInFragment extends Fragment {


    AppCompatButton login_button, login_yandex_button;
    AppCompatEditText edit_text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_in, container,false);
        elementIdentification(view);
        login_button.setEnabled(false);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postEmail();
                    }
                }).start();
                login_button.setClickable(false);
                login_yandex_button.setClickable(false);
            }
        });

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                login_button.setEnabled(isEmailValid(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }


    final private String POST_EMAIL_URL = "http://192.168.144.66:8080/api/signin";

    private void postEmail() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(1000,TimeUnit.MILLISECONDS)
                .build();

        Gson gson = new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("email",(String.valueOf(edit_text.getText())));

        String body = gson.toJson(params);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);

        Request request = new Request.Builder().url(POST_EMAIL_URL)
                .post(requestBody)
                .build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()){
                if (response.code() == 409){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Почта уже существует", Toast.LENGTH_LONG).show();
                            login_button.setClickable(true);
                            login_yandex_button.setClickable(true);

                        }
                    });
                }
            }
            else {
                if (response.code()==200){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Аккаунт создан и код отправлен на почту", Toast.LENGTH_LONG).show();
                            IdentificationEmailFragment fragment = new IdentificationEmailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("email", String.valueOf(edit_text.getText()));

                            fragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.login_screen_layout_cl, fragment).addToBackStack("signIn")
                                    .commit();
                        }
                    });
                }
            }
        } catch (IOException e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "Что-то пошло не так, попробуйте позже", Toast.LENGTH_LONG).show();
//                    login_button.setClickable(true);
//                    login_yandex_button.setClickable(true);
//                    IdentificationEmailFragment fragment = new IdentificationEmailFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("email", String.valueOf(edit_text.getText()));


//                    fragment.setArguments(bundle);
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.login_screen_layout_cl, fragment).addToBackStack("signIn")
//                            .commit();
                }
            });
        }
    }

    void elementIdentification(View view){
        login_button = view.findViewById(R.id.login_button);
        login_yandex_button = view.findViewById(R.id.login_yandex_button);
        edit_text = view.findViewById(R.id.edit_text);

        edit_text.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}