package com.example.medicapp.main_screens.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicapp.R;
import com.example.medicapp.databinding.ViewOrderScreenSelectedPatientBinding;
import com.example.medicapp.main_screens.OrderCheckAnalysisListInterface;
import com.example.medicapp.main_screens.OrderRedactPriceAnalysisInterface;
import com.example.medicapp.main_screens.OrderSelectedPatientsListSizeInterface;
import com.example.medicapp.main_screens.objects.Order;
import com.example.medicapp.main_screens.objects.Profile;
import com.example.medicapp.main_screens.objects.SelectedAnalysis;

import java.util.ArrayList;
import java.util.List;

public class OrderSelectedPatientsAdapter extends RecyclerView.Adapter<OrderSelectedPatientsAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<Order> orderList;
    OrderSelectedPatientsListSizeInterface orderSelectedPatientsListSizeInterface;
    OrderCheckAnalysisListInterface orderCheckAnalysisListInterface;
    OrderRedactPriceAnalysisInterface orderRedactPriceAnalysisInterface;

    public OrderSelectedPatientsAdapter(Context context, List<Order> profileList, OrderSelectedPatientsListSizeInterface orderSelectedPatientsListSizeInterface, OrderRedactPriceAnalysisInterface orderRedactPriceAnalysisInterface) {
        inflater = LayoutInflater.from(context);
        this.orderList = profileList;
        this.orderRedactPriceAnalysisInterface = orderRedactPriceAnalysisInterface;
        this.orderSelectedPatientsListSizeInterface = orderSelectedPatientsListSizeInterface;
    }

    @NonNull
    @Override
    public OrderSelectedPatientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_order_screen_selected_patient, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSelectedPatientsAdapter.ViewHolder holder, int position) {
        Profile profile = orderList.get(position).getProfile();
        Order order = orderList.get(position);
        List<SelectedAnalysis> selectedAnalysisList = orderList.get(position).getAnalysisList();

        holder.binding.nameClose.setText(profile.getSurname()+" "+profile.getName());
        holder.binding.nameOpen.setText(profile.getSurname()+" "+profile.getName());
        if (profile.getSex()==0){
            holder.binding.iconClose.setText("\uD83E\uDDD1");
            holder.binding.iconOpen.setText("\uD83E\uDDD1");
        }
        else {
            holder.binding.iconClose.setText("\uD83D\uDC69");
            holder.binding.iconOpen.setText("\uD83D\uDC69");
        }


        holder.binding.closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.closeView.setVisibility(View.GONE);
                holder.binding.openView.setVisibility(View.VISIBLE);
            }
        });
        holder.binding.patientOpenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.closeView.setVisibility(View.VISIBLE);
                holder.binding.openView.setVisibility(View.GONE);
            }
        });

        orderCheckAnalysisListInterface = new OrderCheckAnalysisListInterface() {
            @Override
            public void getCheckPrice(List<SelectedAnalysis> selectedAnalysisList, boolean check, int price) {
                order.setAnalysisList(selectedAnalysisList);
                int pos = holder.getAdapterPosition();
                orderList.set(pos, order);
                orderRedactPriceAnalysisInterface.getCheckPrice(orderList, check, price);
            }
        };

        OrderSelectedAnalysisAdapter analysisAdapter = new OrderSelectedAnalysisAdapter(inflater.getContext(), selectedAnalysisList, orderCheckAnalysisListInterface);
        holder.binding.analysisList.setAdapter(analysisAdapter);

        holder.binding.removePatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(holder.getAdapterPosition());
                orderSelectedPatientsListSizeInterface.onListSize(orderList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void removeItem(int pos){
        orderList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, orderList.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(Order order){
        orderList.add(order);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewOrderScreenSelectedPatientBinding binding;
        public ViewHolder(@NonNull View view) {
            super(view);
            binding = ViewOrderScreenSelectedPatientBinding.bind(view);
//            patient_spinner = itemView.findViewById(R.id.patient_spinner);
//            analysis_list = itemView.findViewById(R.id.analysis_list);
//            remove_patient_button = itemView.findViewById(R.id.remove_patient_button);
        }
    }
}
