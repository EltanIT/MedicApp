package com.example.medicapp.main_screens.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.medicapp.R;
import com.example.medicapp.main_screens.objects.Order;
import com.example.medicapp.main_screens.objects.Profile;

import java.util.List;

public class OrderDateSpinnerAdapter extends ArrayAdapter<String> {

    LayoutInflater inflater;

    public OrderDateSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> dateList) {
        super(context, resource, dateList);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_place_on_order_select_date, null, true);
        String name = getItem(position);

        TextView name_tv = view.findViewById(R.id.name);
        name_tv.setText(name);
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.view_place_on_order_select_date, parent, false);
        }

        String name = getItem(position);

        TextView name_tv = convertView.findViewById(R.id.name);
        name_tv.setText(name);
        return convertView;
    }
}
