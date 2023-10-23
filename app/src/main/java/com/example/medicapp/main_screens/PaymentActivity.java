package com.example.medicapp.main_screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.medicapp.R;
import com.example.medicapp.databinding.ActivityPaymentBinding;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentActivity extends AppCompatActivity{

    ActivityPaymentBinding binding;
    SharedPreferences preferences;
    String order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPaymentBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        preferences = getSharedPreferences("CartTable", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        if (intent!= null){
            order = intent.getStringExtra("order");
        }

        setting();
        new PostOrder().execute();
        setBinding();

    }


    private void setBinding() {
        binding.getCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.goToMainscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("cart");
                editor.remove("cartPrice");
                editor.commit();
                Intent intent = new Intent(PaymentActivity.this, MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void setting() {
        binding.group2Cl.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(PaymentActivity.this,R.anim.anim_payment_icon);
        binding.paymentIconIv.startAnimation(animation);


    }

    private class PostOrder extends AsyncTask<Void,Void,String> {
        private final String POST_ORDER_URL = "http://192.168.144.66:8080/api/order";

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), order);
            Request request = new Request.Builder().url(POST_ORDER_URL).post(requestBody).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()){
                    return null;
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.statePaymentTv.setText("Производим оплату...");
                        }
                    });

                    if (response.code() == 200){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.progressLl.setVisibility(View.GONE);
                                binding.group2Cl.setVisibility(View.VISIBLE);
                            }
                        });
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PaymentActivity.this, "Ошибка подключения к серверу", Toast.LENGTH_SHORT).show();
                    binding.progressLl.setVisibility(View.GONE);
                    binding.group2Cl.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("cart");
        editor.remove("cartPrice");
        editor.apply();
        Intent intent = new Intent(PaymentActivity.this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}