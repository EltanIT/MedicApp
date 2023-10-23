package com.example.medicapp.main_screens;

import com.example.medicapp.main_screens.objects.Analysis;
import com.example.medicapp.main_screens.objects.Order;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;

import java.util.List;

public interface OrderRedactPriceAnalysisInterface {
    void getCheckPrice(List<Order> orderList, boolean check, int price);
}
