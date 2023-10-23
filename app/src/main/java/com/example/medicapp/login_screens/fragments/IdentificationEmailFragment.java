package com.example.medicapp.login_screens.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.chaos.view.PinView;
import com.example.medicapp.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IdentificationEmailFragment extends Fragment {

    TextView timer_text;
    ImageView back_button;
    PinView edit_text_code_pv;
    private SharedPreferences preferences;

    class PostCode extends AsyncTask<Void,Void,Void>{
        final private String POST_EMAIL_CODE_URL = "http://192.168.144.66:8080/api/confirm";
        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(1000, TimeUnit.MILLISECONDS)
                    .build();

            Gson gson = new Gson();
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("recoveryCode",(String.valueOf(edit_text_code_pv.getText())));

            String body = gson.toJson(params);

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);

            Request request = new Request.Builder()
                    .url(POST_EMAIL_CODE_URL)
                    .post(requestBody)
                    .build();

            try(Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()){
                    if (response.code()== 400){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Неверно введен код, попробуйте снова", Toast.LENGTH_SHORT).show();
                                edit_text_code_pv.setText("");
                                postCode.cancel(true);
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), response.code()+"Ошибка соединения с сервером, попробуйте позже", Toast.LENGTH_SHORT).show();
                                edit_text_code_pv.setText("");
                                postCode.cancel(true);
                            }
                        });
                    }

                }
                else {
                    if (response.code() == 200){
                        ObjectMapper objectMapper = new ObjectMapper();
                        String responseBody = response.body().string();
                        Map<String, String> stringMap = objectMapper.readValue(responseBody, HashMap.class);
                        String text = stringMap.get("token");
                        String token =  text.substring(text.indexOf(".")+1, text.lastIndexOf("."));
                        Log.i("token1111",token);
                        getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    SharedPreferences.Editor editor= preferences.edit();
                                    editor.remove("token");
                                    editor.putString("token", token);
                                    editor.commit();

                                    getActivity().getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.login_screen_layout_cl, new CreatePasswordFragment())
                                            .commit();

//

                                }
                            });


                    }
                }
            } catch (IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Ошибка соединения с сервером, попробуйте позже", Toast.LENGTH_SHORT).show();
                        edit_text_code_pv.setText("");
                        postCode.cancel(true);

//                        getActivity().getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.login_screen_layout_cl, new CreatePasswordFragment())
//                                .commit();
                    }
                });
            }
            return null;
        }
    }

    private String email = null;
    private PostCode postCode = new PostCode();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_identification_email,container, false);
        preferences = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
        elementIdentification(view);
        Bundle bundle = getArguments();
        if (bundle!= null){
            email = bundle.getString("email");
        }
        edit_text_code_pv.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit_text_code_pv, InputMethodManager.SHOW_IMPLICIT);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        edit_text_code_pv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (4 == edit_text_code_pv.getText().length()){
                    postCode.cancel(true);
                    postCode = (PostCode) new PostCode().execute();
                }
            }
        });

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer_text.setText("Отправить код повторно можно будет через "+ (millisUntilFinished / 1000) +" секунд");
                // logic to set the EditText could go here
            }
            public void onFinish() {
                timer_text.setText("Отправить код повторно");
            }
        }.start();
        return view;
    }
    void elementIdentification(View view){
        timer_text = view.findViewById(R.id.timer_text);
        edit_text_code_pv = view.findViewById(R.id.edit_text_code_pv);
        back_button = view.findViewById(R.id.back_button);
    }

}