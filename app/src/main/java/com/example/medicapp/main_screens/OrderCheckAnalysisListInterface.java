package com.example.medicapp.main_screens;

import com.example.medicapp.main_screens.objects.Analysis;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;

import java.util.List;

public interface OrderCheckAnalysisListInterface {
    void getCheckPrice(List<SelectedAnalysis> selectedAnalysisList, boolean check, int price);
}
