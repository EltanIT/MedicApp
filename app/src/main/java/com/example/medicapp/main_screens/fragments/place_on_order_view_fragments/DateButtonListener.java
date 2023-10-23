package com.example.medicapp.main_screens.fragments.place_on_order_view_fragments;

import com.example.medicapp.main_screens.objects.Order;

import java.time.LocalDateTime;
import java.util.Date;

public interface DateButtonListener {
    void ButtonClick(boolean check, LocalDateTime date);
}
