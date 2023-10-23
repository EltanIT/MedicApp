package com.example.medicapp.main_screens.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicapp.R;
import com.example.medicapp.main_screens.objects.Analysis;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CatalogAnalysisAdapter extends RecyclerView.Adapter<CatalogAnalysisAdapter.ViewHolder> {

    List<Analysis> listItem;
    List<SelectedAnalysis> selectedAnalysisList;
    LayoutInflater inflater;

    public interface OnAnalysisClickListener {
        void onAnalysisClick(Analysis analysis, boolean checkCart);
    }

    public interface OnAnalysisButtonClickListener {
        void onAnalysisButtonClick(SelectedAnalysis analysis, boolean action);
    }

    OnAnalysisClickListener onAnalysisClickListener;
    OnAnalysisButtonClickListener onAnalysisButtonClickListener;

    public CatalogAnalysisAdapter(List<Analysis> listItem, Context context, OnAnalysisClickListener onClickListener, OnAnalysisButtonClickListener onAnalysisButtonClickListener, List<SelectedAnalysis> selectedAnalysisList) {
        this.listItem = listItem;
        inflater = LayoutInflater.from(context);
        onAnalysisClickListener = onClickListener;
        this.onAnalysisButtonClickListener = onAnalysisButtonClickListener;
        this.selectedAnalysisList = selectedAnalysisList;
    }

    @NonNull
    @Override
    public CatalogAnalysisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_mainscreen_catalog_analisis, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Analysis analysis = listItem.get(position);
        SelectedAnalysis selectedAnalysis = new SelectedAnalysis(analysis, true);
        Button add_button = holder.add_button;
        boolean checkCart = true;

        holder.name_analysis.setText(analysis.getTitle());
        holder.price_analysis.setText(String.valueOf(analysis.getPrice()));
        holder.day_count.setText(analysis.getDeadline());

        if (selectedAnalysisList.size()>0){
            for (SelectedAnalysis selAnalysis: selectedAnalysisList) {
//                analysis.equals(selAnalysis.getAnalysis())
                if (Objects.equals(analysis.getTitle(), selAnalysis.getAnalysis().getTitle())){
                    selectedAnalysis = selAnalysis;
                    add_button.setSelected(true);
                    checkCart = false;
                    add_button.setText("Убрать");
                    add_button.setTextColor(Color.parseColor("#1A6FEE"));
                    holder.itemView.setClickable(false);
                }
            }
        }


        SelectedAnalysis finalSelectedAnalysis = selectedAnalysis;
        boolean[] finalCheckCart = {checkCart};
        holder.add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_button.setSelected(!add_button.isSelected());

                if(add_button.isSelected()){
//                    Toast.makeText(inflater.getContext(), "Добавлено в корзину", Toast.LENGTH_SHORT).show();
                    add_button.setText("Убрать");
                    add_button.setTextColor(Color.parseColor("#1A6FEE"));
                    onAnalysisButtonClickListener.onAnalysisButtonClick(finalSelectedAnalysis, true);
                    finalCheckCart[0] = false;
//                    database.SaveAnalysis(analysis);
                }
                else{
//                    Toast.makeText(inflater.getContext(), "Удалено из корзины", Toast.LENGTH_SHORT).show();
                    add_button.setText("Добавить");
                    add_button.setTextColor(Color.parseColor("#FFFFFF"));
                    onAnalysisButtonClickListener.onAnalysisButtonClick(finalSelectedAnalysis, false);
                    finalCheckCart[0] = true;
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAnalysisClickListener.onAnalysisClick(analysis, finalCheckCart[0]);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(List<Analysis> analysisList){
        listItem = new ArrayList<>();
        listItem.addAll(analysisList);
        notifyDataSetChanged();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Analysis> analysisList){
        listItem.addAll(analysisList);
        notifyDataSetChanged();
    }

//    public String getDayAddition(int num) {
//
//        int preLastDigit = num % 100 / 10;
//
//        if (preLastDigit == 1) {
//            return "дней";
//        }
//
//        switch (num % 10) {
//            case 1:
//                return "день";
//            case 2:
//            case 3:
//            case 4:
//                return "дня";
//            default:
//                return "дней";
//        }
//
//    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_analysis, price_analysis, day_count;
        AppCompatButton add_button;
        public ViewHolder(@NonNull View view) {
            super(view);
            name_analysis = view.findViewById(R.id.name_analysis);
            price_analysis = view.findViewById(R.id.price_analysis);
            day_count = view.findViewById(R.id.day_count);
            add_button = view.findViewById(R.id.add_button);

        }
    }
}
