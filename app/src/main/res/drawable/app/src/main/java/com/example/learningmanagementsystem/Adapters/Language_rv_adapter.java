package com.example.learningmanagementsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningmanagementsystem.Entities.LanguageDashboardEntity;
import com.example.learningmanagementsystem.Fragments.medium_fragment;
import com.example.learningmanagementsystem.R;

import java.util.ArrayList;

public class Language_rv_adapter extends RecyclerView.Adapter<Language_rv_adapter.ViewHolder>
{
    private Activity activity;
    private ArrayList<LanguageDashboardEntity> list;
    private Context context;

    public Language_rv_adapter(Activity activity, ArrayList<LanguageDashboardEntity> list,Context context)
    {
        this.activity = activity;
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v=LayoutInflater.from(activity).inflate(R.layout.language_rv_layout,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.name.setText(list.get(position).getName());
        holder.discription.setText(list.get(position).getDiscription());
        holder.image.setImageResource(list.get(position).getImaage());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView name,discription;
        ImageView image;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.language_rv_name);
            discription=itemView.findViewById(R.id.language_rv_describtion);
            image=itemView.findViewById(R.id.language_rv_image);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String language=list.get(getAdapterPosition()).getName();
                    Bundle bundle=new Bundle();
                    bundle.putString(activity.getString(R.string.bundle_language),language);
                    medium_fragment mediumFragment=new medium_fragment();
                    mediumFragment.setArguments(bundle);
                    FragmentActivity fragmentActivity= (FragmentActivity) activity;
                    fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_dashboard_frame_layout,mediumFragment).commit();


                }
            });
        }
    }
}
