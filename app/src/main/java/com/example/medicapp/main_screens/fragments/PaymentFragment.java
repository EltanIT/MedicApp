package com.example.medicapp.main_screens.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.medicapp.R;
import com.example.medicapp.databinding.FragmentPaymentBinding;
import com.example.medicapp.main_screens.MainScreen;
import com.example.medicapp.main_screens.objects.Order;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentFragment extends Fragment implements MainScreen.BackpressedListener {
    FragmentPaymentBinding binding;
    SharedPreferences preferences;
    String order;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentPaymentBinding.inflate(inflater,container, false );
        preferences = getActivity().getSharedPreferences("CartTable", Context.MODE_PRIVATE);

        Bundle bundle = getArguments();
        if (bundle!= null){
            order = bundle.getString("order");
        }

        setting();
        new PostOrder().execute();
        setBinding();
        return binding.getRoot();
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
                Intent intent = new Intent(getActivity(), MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void setting() {
        binding.group2Cl.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_payment_icon);
        binding.paymentIconIv.startAnimation(animation);

    }

    private class PostOrder extends AsyncTask<Void,Void,String> {
        private final String POST_ORDER_URL = "http://192.168.144.66:8080/api/order";

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), order);
            Request request = new Request.Builder().url(POST_ORDER_URL).post(requestBody).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return null;
                } else {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.statePaymentTv.setText("Производим оплату...");
                        }
                    });
                    if (response.code() == 200) {
                        try {
                            Thread.sleep(1000);
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.progressLl.setVisibility(View.GONE);
                                    binding.group2Cl.setVisibility(View.VISIBLE);
                                }
                            });
                            return "";
                        } catch (InterruptedException e) {
                            return null;
                        }
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

//                requireActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), "Ошибка подключения к серверу", Toast.LENGTH_SHORT).show();
//
////                    binding.progressLl.setVisibility(View.GONE);
////                    binding.group2Cl.setVisibility(View.VISIBLE);
//                    }
//                });
                cancel(true);
//                requireActivity().getSupportFragmentManager().popBackStack();

                try {
                    Thread.sleep(1000);
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.progressLl.setVisibility(View.GONE);
                            binding.group2Cl.setVisibility(View.VISIBLE);
                        }
                    });

                } catch (InterruptedException e) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                }

            }

        }
    }

    @Override
    public boolean onBackPressed() {
        if (binding.progressLl.getVisibility() == View.GONE){
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("cart");
            editor.remove("cartPrice");
            editor.apply();
            Intent intent = new Intent(getActivity(), MainScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }
}