package com.example.learningapp.RvAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.Entity.SavedPages;
import com.example.learningapp.R;
import com.example.learningapp.WebViewSupport.Show_saved_pages;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShowSavedRvAdapter extends RecyclerView.Adapter<ShowSavedRvAdapter.ViewHolder>
{
    private ArrayList<SavedPages> list;
    private Activity context;
    private LinearLayout upperLayout;

    public ShowSavedRvAdapter(ArrayList<SavedPages> list, Activity context)
    {
        this.list = list;
        this.context = context;
    }

    public ShowSavedRvAdapter(ArrayList<SavedPages> list, Activity context, LinearLayout upperLayout)
    {
        this.list = list;
        this.context = context;
        this.upperLayout = upperLayout;
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
        ImageView delete;

        public ViewHolder(@NonNull final View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.show_saved_layout_title);
            url = itemView.findViewById(R.id.show_saved_layout_url);
            delete = itemView.findViewById(R.id.show_saved_layout_deleteimg);

            delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this bookmark").setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    SavedPages savedPages = list.get(getAdapterPosition());


                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                                    DocumentReference documentReference = firestore.collection(context.getString(R.string.firestore_collection_saved_pages))
                                            .document(user.getUid()).collection(context.getString(R.string.saved_pages_collection_path)).document(savedPages.getId());

                                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Snackbar.make(upperLayout, "Deleted successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
                                            context.startActivity(new Intent(context, Show_saved_pages.class));
                                            context.finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            Snackbar.make(upperLayout, "Error while deleting", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }


    }
}
