package com.example.medicapp.main_screens.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medicapp.R;
import com.example.medicapp.databinding.FragmentProfileBinding;
import com.example.medicapp.main_screens.objects.Profile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    private final static int GALLERY_PICK = 1;
    private byte sex = 0;
    private List<String> sexList = new ArrayList<>();
    private Uri imageUri;
    private Profile profile = new Profile();

    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        preferences = getActivity().getSharedPreferences("Table", Context.MODE_PRIVATE);
//        TextView text1 = getView().findViewById(R.id.text1);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new GetProfile().execute();
//                new GetImage().execute();
//            }
//        }).start();
        Gson gson = new Gson();
        profile = gson.fromJson(preferences.getString("profile", null), Profile.class);
        settingProfile();
        setBinding();

        return binding.getRoot();
    }

    private void settingProfile() {
        binding.nameEt.setText(profile.getName());
        binding.surnameEt.setText(profile.getSurname());
        binding.patronymicEt.setText(profile.getPatronymic());
        binding.dateBirthdayEt.setText(profile.getDob());
        int sex = profile.getSex();
        if (sex == 0){
            sexList.add("Мужской");
            sexList.add("Женский");
        }
        else{
            sexList.add("Женский");
            sexList.add("Мужской");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sexList);
        binding.sexS.setAdapter(adapter);
    }
    private void setBinding() {
        binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_PICK);
            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(binding.nameEt.getText());
                String patronymicEt = String.valueOf(binding.patronymicEt.getText());
                String surnameEt = String.valueOf(binding.surnameEt.getText());
                byte sex = (byte) binding.sexS.getSelectedItemPosition();
                String birthday = String.valueOf(binding.dateBirthdayEt.getText());
                Profile profile = new Profile(name,patronymicEt,birthday,surnameEt,sex);
                saveProfile(profile);


//                   if (binding.avatarIv.getDrawable() == getResources().getDrawable(R.drawable.default_avatar)){
//                        newProfile = new Profile(String.valueOf(binding.nameEt.getText()),String.valueOf(binding.patronymicEt.getText()),String.valueOf(binding.surnameEt.getText()),String.valueOf(binding.sexEt.getText()));
//                        profileDB.saveProfile(newProfile);
//                   }
//                   else {
//                       newProfile = new Profile(imageUri,name,patronymicEt,surnameEt,sexEt);
//                       if (newProfile.getName().equals("name")){
//                           Toast.makeText(getContext(),"Профиль сохранен", Toast.LENGTH_SHORT).show();
//                       }
//                       else {
//                           profileDB.saveProfile(newProfile);
//                       }
//
//                   }

            }
        });

        binding.nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.length() ==0)){
                    binding.nameEt.setActivated(true);
                    binding.nameEt.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    binding.nameEt.setActivated(false);
                    binding.nameEt.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.patronymicEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.length() ==0)){
                    binding.patronymicEt.setActivated(true);
                    binding.patronymicEt.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    binding.patronymicEt.setActivated(false);
                    binding.patronymicEt.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.surnameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.length() ==0)){
                    binding.surnameEt.setActivated(true);
                    binding.surnameEt.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    binding.surnameEt.setActivated(false);
                    binding.surnameEt.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.dateBirthdayEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.length() ==0)){
                    binding.dateBirthdayEt.setActivated(true);
                    binding.dateBirthdayEt.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    binding.dateBirthdayEt.setActivated(false);
                    binding.dateBirthdayEt.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    void saveProfile(Profile profile){
        Gson gson = new Gson();
        String profileJson = gson.toJson(profile);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("profile");
        editor.putString("profile", profileJson);
        editor.commit();

//        new PutProfile().execute(profileJson);
    }

    public void getProfile(){
        new GetProfile().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            binding.avatarIv.setImageURI(imageUri);

            binding.avatarIv.setDrawingCacheEnabled(true);
            Bitmap bm = binding.avatarIv.getDrawingCache();

            File fPath = new File((getActivity()
                    .getApplicationContext().getFileStreamPath("profileIcon.png")
                    .getPath()));

            try {
                FileOutputStream strm = new FileOutputStream(fPath);
                bm.compress(Bitmap.CompressFormat.PNG, 80, strm);
                strm.close();

                new PostImage().execute();
            }
            catch (IOException e){
                binding.avatarIv.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
            }

        }
    }

    private class PutProfile extends AsyncTask<String, Void, String> {
        final private String PUT_PROFILE_URL = "http://192.168.144.66:8080/api/profile";

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();

            RequestBody requestBody = new FormBody.Builder()
                    .add("profile",strings[0])
                    .build();
            Request request = new Request.Builder().url(PUT_PROFILE_URL).put(requestBody).build();
            try(Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()){
                    return null;
                }
                else {
                    if (response.code() == 200){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Профиль успешно изменен/создан", Toast.LENGTH_LONG).show();
                            }
                        });
                        return "";
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
                        Toast.makeText(getContext(), "Что-то пошло не так, попробуйте позже", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else if (!s.equals("")){

            }
        }
    }
    private class PostImage extends AsyncTask<Void, Void, String> {
        final private String POST_IMAGE_URL = "http://192.168.144.66:8080/api/profileIcon";

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();

            Gson gson = new Gson();

            File fPath = new File((getActivity()
                    .getApplicationContext().getFileStreamPath("profileIcon.png")).getPath());
            File f= new File(fPath.getPath());

            if (f.exists()){
                Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(b));
                Request request = new Request.Builder().url(POST_IMAGE_URL).post(body).build();
                try(Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()){
                        return null;
                    }
                    else {
                        if (response.code() == 200){
                            return "";
                        }
                    }
                } catch (IOException e) {
                    return null;
                }
            }



//            RequestBody body = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("file", fPath.getName(),RequestBody.create(MediaType.parse("image/png"), f))
//                    .build();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s==null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Что-то пошло не так, попробуйте позже", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else if (s.equals("")){

            }
        }
    }

    private class GetProfile extends AsyncTask<Void, Void, String> {
        final private String GET_PROFILE_URL = "http://192.168.144.66:8080/api/profile";

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(3000, TimeUnit.MILLISECONDS)
                    .build();;

            Request request = new Request.Builder().url(GET_PROFILE_URL).build();
            try(Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()){
                    return null;
                }
                else {
                    if (response.code() == 200){
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                profile = gson.fromJson(String.valueOf(response.body()), Profile.class);
                                settingProfile();
                            }
                        });
                        return "";
                    }
                    else if (response.code()== 204){
                        return "null";
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
            if (s!=null){
                if (s.equals("null")){
                    profile = new Profile("", "", "", "", (byte) 0);
                    settingProfile();
                }
            }
        }
    }
    private class GetImage extends AsyncTask<Void, Void, String> {
        final private String GET_IMAGE_URL = "http://192.168.144.66:8080/api/profileIcon/";

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();

            Request request = new Request.Builder().url(GET_IMAGE_URL).build();
            try(Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()){
                    return null;
                }
                else {
                    if (response.code() == 200){
                        Gson gson = new Gson();
                        Bitmap bitmap = gson.fromJson(response.body().charStream(), Bitmap.class);
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.avatarIv.setImageBitmap(bitmap);
                            }
                        });
                        return "";

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

            }
            else if (s.equals("")){

            }
        }
    }

}