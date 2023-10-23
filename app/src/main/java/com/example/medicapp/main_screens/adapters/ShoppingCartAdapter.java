package com.example.medicapp.main_screens.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicapp.R;
import com.example.medicapp.main_screens.objects.Analysis;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    List<SelectedAnalysis> listItem;
    LayoutInflater inflater;

    public interface OnClickRedactPrice{
        void redactPrice(int price,List<SelectedAnalysis> selectedAnalysisList, boolean redactBool);
    }
    public interface OnClickRedactCountPatient{
        void redactPrice(int price, boolean redactBool);
    }

    OnClickRedactPrice onClickRedactPrice;
    OnClickRedactCountPatient onClickRedactCountPatient;

    public ShoppingCartAdapter(List<SelectedAnalysis> listItem, Context context, OnClickRedactPrice onClickRedactPrice, OnClickRedactCountPatient onClickRedactCountPatient) {
        this.listItem = listItem;
        inflater = LayoutInflater.from(context);
        this.onClickRedactPrice = onClickRedactPrice;
        this.onClickRedactCountPatient = onClickRedactCountPatient;
    }

    @NonNull
    @Override
    public ShoppingCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_shopping_cart_analysis, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartAdapter.ViewHolder holder, int position) {
        Analysis analysis = listItem.get(position).getAnalysis();

        holder.name_analysis.setText(analysis.getTitle());
        holder.price_analysis.setText(String.valueOf(analysis.getPrice()));
        holder.add_patient_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oldCount = Integer.parseInt((String) holder.count_patient.getText());
                holder.count_patient.setText(String.valueOf(oldCount+1));
                onClickRedactCountPatient.redactPrice(analysis.getPrice(), true);
            }
        });
        holder.remove_patient_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oldCount = Integer.parseInt((String) holder.count_patient.getText());
                if (oldCount == 1){
                    return;
                }
                holder.count_patient.setText(String.valueOf(oldCount-1));
                onClickRedactCountPatient.redactPrice(analysis.getPrice(), false);
            }
        });

        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(holder.getAdapterPosition());
                onClickRedactPrice.redactPrice(analysis.getPrice(), listItem, false);
                Toast.makeText(inflater.getContext(), "Удалено", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void removeItem(int position){
        listItem.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listItem.size());
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_analysis, price_analysis, count_patient;
        ImageView remove_patient_button, add_patient_button, delete_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_analysis = itemView.findViewById(R.id.name_analysis);
            price_analysis = itemView.findViewById(R.id.price_analysis);
            count_patient = itemView.findViewById(R.id.count_patient);

            remove_patient_button = itemView.findViewById(R.id.remove_patient_button);
            add_patient_button = itemView.findViewById(R.id.add_patient_button);
            delete_item = itemView.findViewById(R.id.delete_item);
        }
    }
}
