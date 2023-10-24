package com.example.medicapp.main_screens.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicapp.R;
import com.example.medicapp.main_screens.objects.News;
import com.google.gson.Gson;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<News> itemList;
    LayoutInflater inflater;

    public NewsAdapter(List<News> itemList, Context context) {
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_mainscreen_news, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = itemList.get(position);
        holder.banner_iv.setImageResource(Integer.parseInt(news.getImage()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView banner_iv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            banner_iv = itemView.findViewById(R.id.banner_iv);
        }
    }
}
