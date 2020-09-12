package com.example.learningapp.RvAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.Entity.SavedPages;
import com.example.learningapp.R;

import java.util.ArrayList;

public class ShowSavedRvAdapter extends RecyclerView.Adapter<ShowSavedRvAdapter.ViewHolder>
{
    private ArrayList<SavedPages> list;
    private Activity context;

    public ShowSavedRvAdapter(ArrayList<SavedPages> list, Activity context)
    {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.show_saved_rv_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.title.setText(list.get(position).getTitle());
        holder.url.setText(list.get(position).getUrl());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, url;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.show_saved_layout_title);
            url = itemView.findViewById(R.id.show_saved_layout_url);
        }
    }
}
