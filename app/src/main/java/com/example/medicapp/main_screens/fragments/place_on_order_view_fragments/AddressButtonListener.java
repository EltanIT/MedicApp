package com.example.medicapp.main_screens.fragments.place_on_order_view_fragments;

import com.example.medicapp.main_screens.objects.Order;

public interface AddressButtonListener {
    void ButtonClick(boolean check, Order.ClientAddress address);
}
