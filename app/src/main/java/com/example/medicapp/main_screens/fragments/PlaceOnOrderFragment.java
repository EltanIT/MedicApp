package com.example.medicapp.main_screens.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.medicapp.R;
import com.example.medicapp.databinding.FragmentPlaceOnOrderBinding;
import com.example.medicapp.main_screens.OrderRedactPriceAnalysisInterface;
import com.example.medicapp.main_screens.OrderSelectedPatientsListSizeInterface;
import com.example.medicapp.main_screens.adapters.OrderSelectedPatientsAdapter;
import com.example.medicapp.main_screens.fragments.place_on_order_view_fragments.AddPatientFragment;
import com.example.medicapp.main_screens.fragments.place_on_order_view_fragments.AddPatientListener;
import com.example.medicapp.main_screens.fragments.place_on_order_view_fragments.AddressButtonListener;
import com.example.medicapp.main_screens.fragments.place_on_order_view_fragments.AddressFragment;
import com.example.medicapp.main_screens.fragments.place_on_order_view_fragments.DateButtonListener;
import com.example.medicapp.main_screens.fragments.place_on_order_view_fragments.DateFragment;
import com.example.medicapp.main_screens.objects.Order;
import com.example.medicapp.main_screens.objects.Profile;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PlaceOnOrderFragment extends Fragment {
    private FragmentPlaceOnOrderBinding binding;
    private List<Order> orderList = new ArrayList<>();
    private List<SelectedAnalysis> selectedAnalyseList = new ArrayList<>();
    private OrderSelectedPatientsListSizeInterface orderSelectedPatientsListSizeInterface;
    private OrderRedactPriceAnalysisInterface orderRedactPriceAnalysisInterface;
    private int priceNow;
    private int priceView;
    private int priceAllAnalysis;
    private int countSelectedAnalyseList;
    private int countAllAnalysis;
    private int countAllAnalysisView;
    private OrderSelectedPatientsAdapter patientsAdapter;
    private SharedPreferences preferences;
    private Profile profile;
    private Order.ClientAddress clientAddress;
    private Order.ClientAddress clientAddressBack;
    private LocalDateTime date = LocalDateTime.now();
    private Map<String, Object> order = new HashMap<>();
    private Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaceOnOrderBinding.inflate(inflater, container,false);
        preferences = requireActivity().getSharedPreferences("Table", Context.MODE_PRIVATE);
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setInterface();
                setting();
                getAddress();
            }
        });
        setBinding();

        return binding.getRoot();
    }

    private void setInterface() {
        orderSelectedPatientsListSizeInterface = new OrderSelectedPatientsListSizeInterface() {
            @Override
            public void onListSize(List<Order> getOrderList) {
                orderList = getOrderList;

                priceNow -= priceAllAnalysis;
                priceView = priceNow;
                countSelectedAnalyseList -= selectedAnalyseList.size();

                countAllAnalysis -= selectedAnalyseList.size();
                countAllAnalysisView = countAllAnalysis;

                binding.priceView.setText(priceNow+" ₽");
                binding.countAnalysisView.setText(countAllAnalysis+getAnalysisAddition(countAllAnalysis));

            }
        };

        orderRedactPriceAnalysisInterface = new OrderRedactPriceAnalysisInterface() {
            @Override
            public void getCheckPrice(List<Order> getOrderList, boolean check, int price) {
                orderList = getOrderList;

                if (check){
                    priceView += price;
                    countAllAnalysisView += 1;
                }
                else {
                    priceView -= price;
                    countAllAnalysisView -= 1;
                    countSelectedAnalyseList -=1;
                }
                binding.countAnalysisView.setText(countAllAnalysisView+getAnalysisAddition(countAllAnalysisView));
                binding.priceView.setText(priceView+" ₽");
            }

        };

        addressButtonListener = new AddressButtonListener() {
            @Override
            public void ButtonClick(boolean check, Order.ClientAddress address) {
                checkAddress = true;
                if (check){
                    clientAddress = address;
                    clientAddressBack = address;
                    binding.selectAddressView.setActivated(false);
                    binding.addressTextView.setText(clientAddress.getAddress()+", кв. "+clientAddress.getFlat());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveAddress();
                        }
                    }).start();
                    checkMainButton();
                }
                else {
                    clientAddressBack = address;
                }

            }
        };
        dateButtonListener = new DateButtonListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void ButtonClick(boolean check, LocalDateTime dateTime) {
                    checkDate = check;
                    date = dateTime;

                    String month = date.getMonth().getDisplayName(TextStyle.FULL, new Locale("ru", "RU"));
                    String dateTextView_text = "Сегодня, "+date.getDayOfMonth()+" "+month+", "+ date.getHour()+":"+"00";

                    binding.dateTextView.setTextColor(getResources().getColor(R.color.black));
                    binding.dateTextView.setText(dateTextView_text);
                    checkMainButton();
            }
        };
        addPatientListener = new AddPatientListener() {
            @Override
            public void ButtonClick(Profile profile) {
                orderList.add(new Order(profile, selectedAnalyseList));

                patientsAdapter = new OrderSelectedPatientsAdapter(getContext(), orderList, orderSelectedPatientsListSizeInterface, orderRedactPriceAnalysisInterface);
                binding.selectedPatientList.setAdapter(patientsAdapter);
//                patientsAdapter.addItem(orderList.get(orderList.size()-1));

                priceNow += priceAllAnalysis;
                priceView += priceAllAnalysis;
                countSelectedAnalyseList += selectedAnalyseList.size();
                countAllAnalysis += selectedAnalyseList.size();
                countAllAnalysisView += selectedAnalyseList.size();

                binding.priceView.setText(priceNow + " ₽");
                binding.countAnalysisView.setText(countAllAnalysis+getAnalysisAddition(countSelectedAnalyseList));
            }
        };
    }

    private void saveAddress() {
        if (clientAddress.getBuildingType()==1){
            SharedPreferences.Editor editor = preferences.edit();
            String addressJson = gson.toJson(clientAddress);
            editor.remove("address");
            editor.putString("address", addressJson);
            editor.apply();
        }
        else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("address");
            editor.apply();
        }


    }
    private void getAddress(){
        String addressJson = preferences.getString("address", "");
        if (!addressJson.equals("")){
            clientAddress = gson.fromJson(addressJson, Order.ClientAddress.class);
            clientAddressBack = clientAddress;
            binding.selectAddressView.setActivated(false);
            checkAddress=true;
            binding.addressTextView.setText(clientAddress.getAddress()+", кв. "+clientAddress.getFlat());
        }
    }

    private void setting(){

        Bundle bundle = getArguments();
        assert bundle != null;
        selectedAnalyseList = bundle.getParcelableArrayList("analysisList");
        priceNow = bundle.getInt("price");
        priceView = priceNow;

        profile = getProfile();
        orderList.add(new Order(profile,selectedAnalyseList));

        priceAllAnalysis = priceNow;
        countSelectedAnalyseList = selectedAnalyseList.size();
        countAllAnalysis = countSelectedAnalyseList;
        countAllAnalysisView = countSelectedAnalyseList;

        binding.priceView.setText(priceNow +" ₽");
        binding.countAnalysisView.setText(countSelectedAnalyseList + getAnalysisAddition(selectedAnalyseList.size()));

        patientsAdapter = new OrderSelectedPatientsAdapter(getContext(), orderList, orderSelectedPatientsListSizeInterface, orderRedactPriceAnalysisInterface);

        binding.selectedPatientList.setAdapter(patientsAdapter);
        binding.continuePlaceOrderButton.setEnabled(false);

        Selection.setSelection(binding.phoneNumber.getText(), binding.phoneNumber.getText().length());
    }

    private void setBinding(){
        binding.continuePlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkNumber){
                    Toast.makeText(getContext(), "Неверный формат номера", Toast.LENGTH_SHORT).show();
                }
                else {
                    createOrder();
                    PaymentFragment paymentFragment = new PaymentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("order", gson.toJson(order));
                    paymentFragment.setArguments(bundle);

                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.most_mainscreen_view, paymentFragment, "placeOrder").addToBackStack("payment")
                            .commit();
                }

            }
        });

        binding.phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    checkPhoneET = true;
                    checkMainButton();
                }else {
                    checkPhoneET = false;
                    checkMainButton();
                }
                if ((charSequence.length() == 10 && charSequence.toString().startsWith(String.valueOf(9)))){
                    checkNumber = true;
                }
                else {
                    checkNumber = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                String formatted;
//                String regex1 = "(\\+\\d)(\\d{3})";
//                String regex2 = "(.+ )(\\d{3})$";
//                String regex3 = "(.+\\-)(\\d{2})$";
//
//                if (s.toString().matches(regex1)) {
//                    formatted = String.valueOf(s).replaceFirst(regex1, "$1 ($2) ");
//                    binding.phoneNumber.setText(formatted);
//                    binding.phoneNumber.setSelection(formatted.length());
//                } else if (s.toString().matches(regex2)) {
//                    formatted = String.valueOf(s).replaceFirst(regex2, "$1$2-");
//                    binding.phoneNumber.setText(formatted);
//                    binding.phoneNumber.setSelection(formatted.length());
//                } else if (s.toString().matches(regex3) && s.length() < 18) {
//                    formatted = String.valueOf(s).replaceFirst(regex3, "$1$2-");
//                    binding.phoneNumber.setText(formatted);
//                    binding.phoneNumber.setSelection(formatted.length());
//                }
            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        binding.selectAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                AddressFragment addressFragment = new AddressFragment(addressButtonListener);
                bundle.putParcelable("address", (Parcelable) clientAddressBack);
                addressFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.most_mainscreen_view, addressFragment, "address")
                        .addToBackStack("addressFragment")
                        .commit();
            }
        });
        binding.selectDateView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                DateFragment dateFragment = new DateFragment(dateButtonListener);
                bundle.putInt("date", date.getHour());
                dateFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.most_mainscreen_view, dateFragment, "date")
                        .addToBackStack("dateFragment")
                        .commit();
            }
        });
        binding.addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPatientFragment addPatientFragment = new AddPatientFragment(addPatientListener);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.most_mainscreen_view, addPatientFragment, "patient")
                        .addToBackStack("patientFragment")
                        .commit();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOrder() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> patientsTakingTestsList = new ArrayList<>();

        String comment = "";
        if (binding.commentsEt.getText().length()!=0){
            comment = String.valueOf(binding.commentsEt.getText());
        }
        Map<String, Object> classAddress = objectMapper.convertValue(clientAddress, Map.class);

        Map<String, Object> classPatient = objectMapper.convertValue(profile, Map.class);
        patientsTakingTestsList.add(classPatient);

        order.put("clientAddress", classAddress);
        order.put("date", date.toString());
        order.put("patientsTakingTests", patientsTakingTestsList);
        order.put("phone", String.valueOf(binding.phoneNumber.getText()));
        order.put("price" ,priceNow);
        order.put("comment", comment);
    }

    private Profile getProfile(){
        Gson gson = new Gson();
       String profileJson = preferences.getString("profileJson", null);
       if (profileJson != null){
           Profile profile = gson.fromJson(profileJson, Profile.class);
           return profile;
       }
       else {
           Toast.makeText(getActivity(),"Создайте профиль для оформления заказа", Toast.LENGTH_SHORT).show();
           requireActivity().getSupportFragmentManager().popBackStack();
       }
        return new Profile();
    }
    public String getAnalysisAddition(int num) {

        int preLastDigit = num % 100 / 10;

        if (preLastDigit == 1) {
            return " анализ";
        }

        switch (num % 10) {
            case 1:
                return " анализ";
            case 2:
            case 3:
            case 4:
                return " анализа";
            default:
                return " анализов";
        }

    }

    private boolean checkAddress = false;
    private  boolean checkPhoneET = false;
    private boolean checkNumber = false;
    private  boolean checkDate = false;

    private void checkMainButton(){
        if (checkAddress && checkDate && checkPhoneET && priceNow >= 0){
            binding.continuePlaceOrderButton.setEnabled(true);
        }
        else {
            binding.continuePlaceOrderButton.setEnabled(false);
        }
    }


    private void openView(View viewCl, View viewCard){
        viewCl.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_mainscreen_open_datadialog);
        viewCard.setAnimation(animation);
    }

    private void closeView(View viewCl, View viewCard){
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_mainscreen_close_datadialog);
        viewCard.setAnimation(animation);
        viewCl.setVisibility(View.GONE);
    }



    AddressButtonListener addressButtonListener;
    DateButtonListener dateButtonListener;
    AddPatientListener addPatientListener;

}