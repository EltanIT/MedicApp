package com.example.medicapp.main_screens.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicapp.R;
import com.example.medicapp.main_screens.OrderCheckAnalysisListInterface;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;

import java.util.List;

public class OrderSelectedAnalysisAdapter extends RecyclerView.Adapter<OrderSelectedAnalysisAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<SelectedAnalysis> analysisList;
    OrderCheckAnalysisListInterface orderCheckAnalysisListInterface;

    public OrderSelectedAnalysisAdapter(Context context, List<SelectedAnalysis> analysisList,  OrderCheckAnalysisListInterface orderCheckAnalysisListInterface) {
        inflater = LayoutInflater.from(context);
        this.analysisList = analysisList;
        this.orderCheckAnalysisListInterface = orderCheckAnalysisListInterface;
    }

    @NonNull
    @Override
    public OrderSelectedAnalysisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_order_screen_selected_analysis, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSelectedAnalysisAdapter.ViewHolder holder, int position) {
        SelectedAnalysis analysis = analysisList.get(position);

        holder.name.setText(analysis.getAnalysis().getTitle());
        holder.price_view.setText(analysis.getAnalysis().getPrice() + " ₽");
        holder.checked_analysis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    analysis.setCheck(b);
                    int pos = holder.getAdapterPosition();
                    if (b){
                        holder.name.setTextColor(inflater.getContext().getResources().getColor(R.color.black));
                        holder.price_view.setTextColor(inflater.getContext().getResources().getColor(R.color.black));
                    }
                    else {
                        holder.name.setTextColor(inflater.getContext().getResources().getColor(R.color.Caption_low));
                        holder.price_view.setTextColor(inflater.getContext().getResources().getColor(R.color.Caption_low));
                    }
                    analysisList.set(pos,analysis);
                    orderCheckAnalysisListInterface.getCheckPrice(analysisList, b, analysis.getAnalysis().getPrice());
            }
        });
        holder.analysis_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checked_analysis.setChecked(!holder.checked_analysis.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return analysisList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checked_analysis;
        TextView name, price_view;
        ConstraintLayout analysis_view;
        public ViewHolder(@NonNull View view) {
            super(view);
            checked_analysis = view.findViewById(R.id.checked_analysis);
            name = view.findViewById(R.id.name);
            price_view = view.findViewById(R.id.price_view);
            analysis_view = view.findViewById(R.id.analysis_view);
        }
    }
}
