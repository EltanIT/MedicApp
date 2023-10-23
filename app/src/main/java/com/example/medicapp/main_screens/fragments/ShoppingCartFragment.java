package com.example.medicapp.main_screens.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicapp.R;
import com.example.medicapp.main_screens.MainScreen;
import com.example.medicapp.main_screens.RedactSelectedAnalysisInCartInterface;
import com.example.medicapp.main_screens.adapters.ShoppingCartAdapter;
import com.example.medicapp.main_screens.objects.Analysis;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ShoppingCartFragment extends Fragment {
    private List<SelectedAnalysis> analysisList = new ArrayList<>();
    private ShoppingCartAdapter adapter;
    private RecyclerView cart_rv;
    private ImageView delete_all, back_button;
    private TextView price_view;
    private Button continue_button;
    private int priceNow;
    private int priceNowView;
    private RedactSelectedAnalysisInCartInterface redactSelectedAnalysisInCartInterface;
    private SharedPreferences preferences;

    public ShoppingCartFragment(RedactSelectedAnalysisInCartInterface redactSelectedAnalysisInCartInterface) {
        this.redactSelectedAnalysisInCartInterface = redactSelectedAnalysisInCartInterface;
    }

    public ShoppingCartFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        preferences = getActivity().getSharedPreferences("Cart", Context.MODE_PRIVATE);
        elementIdentification(view);

        Bundle bundle = getArguments();
        if(bundle != null){
            analysisList = bundle.getParcelableArrayList("analysisList");
            priceNow = Integer.parseInt(bundle.getString("price"));
            priceNowView = priceNow;
            price_view.setText(priceNow+"");
        }
        else{
            Toast.makeText(getContext(), "Корзина пуста", Toast.LENGTH_SHORT).show();
        }

        ShoppingCartAdapter.OnClickRedactPrice onClickRedactPrice = new ShoppingCartAdapter.OnClickRedactPrice() {
            @Override
            public void redactPrice(int price, List<SelectedAnalysis> selectedAnalysisList, boolean redactBool) {
                analysisList = selectedAnalysisList;
                redactPriceView(price);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveCartList();
                    }
                }).start();
                redactSelectedAnalysisInCartInterface.RedactSelectedAnalysis(selectedAnalysisList, priceNow);
                if (priceNow == 0){
                    requireActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        };

        ShoppingCartAdapter.OnClickRedactCountPatient onClickRedactCountPatient = new ShoppingCartAdapter.OnClickRedactCountPatient() {
            @Override
            public void redactPrice(int price, boolean redactBool) {
                redactPriceViewCountPatient(price, redactBool);
            }
        };

        adapter = new ShoppingCartAdapter(analysisList, getContext(), onClickRedactPrice, onClickRedactCountPatient);
        cart_rv.setAdapter(adapter);

        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Очистить корзину?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clickYes();
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();

            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceOnOrderFragment placeOnOrderFragment = new PlaceOnOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("analysisList", (ArrayList<? extends Parcelable>) analysisList);
                bundle.putInt("price", priceNow);
                placeOnOrderFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                       .replace(R.id.most_mainscreen_view, placeOnOrderFragment)
                       .addToBackStack("order")
                       .commit();
            }
        });

        return view;
    }

    void clickYes(){
        analysisList.clear();
        redactSelectedAnalysisInCartInterface.RedactSelectedAnalysis(analysisList, 0);
        ((MainScreen)requireActivity()).closeBottomNavigation(View.VISIBLE);
        requireActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    void elementIdentification(View view){
        cart_rv = view.findViewById(R.id.cart_rv);
        delete_all = view.findViewById(R.id.delete_all);
        price_view = view.findViewById(R.id.price_view);
        back_button = view.findViewById(R.id.back_button);
        continue_button = view.findViewById(R.id.continue_button);
    }

    void redactPriceView(int price){

        priceNow -= price;
        priceNowView = price;

        if (priceNow == 0){
            analysisList.clear();
            requireActivity().getSupportFragmentManager().popBackStack();
        }

        price_view.setText(String.valueOf(priceNow));

    }
    void redactPriceViewCountPatient(int price, boolean redactBoll){

        byte mark = 1;
        if (!redactBoll){
            mark = -1;
        }
        int oldPrice = Integer.parseInt(String.valueOf(price_view.getText()));
        priceNowView = price*mark+oldPrice;

        price_view.setText(String.valueOf(priceNowView));

    }

    Gson gson = new Gson();
    Type listType = new TypeToken<List<SelectedAnalysis>>() {}.getType();
    private void saveCartList() {
        SharedPreferences.Editor editor = preferences.edit();
        String cartListJson = gson.toJson(analysisList, listType);
        editor.remove("cart");
        editor.putString("cart", cartListJson);
        editor.commit();
    }
}