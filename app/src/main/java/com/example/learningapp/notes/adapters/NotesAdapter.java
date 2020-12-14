package com.example.learningapp.notes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.notes.entities.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>
{
    private List<Note> notes;

    public NotesAdapter(List<Note> notes)
    {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new NotesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cotainer_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position)
    {
        holder.setNote(notes.get(position));
    }

    @Override
    public int getItemCount()
    {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder
    {
        TextView textTitle, textSubTitle, textDateTime;

        public NotesViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textSubTitle = itemView.findViewById(R.id.textSubTiitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
        }

        void setNote(Note note)
        {
            textTitle.setText(note.getTitle());
            if (note.getSubTitle().trim().isEmpty())
            {
                textSubTitle.setVisibility(View.GONE);
            } else
            {
                textSubTitle.setText(note.getSubTitle());
            }
            textDateTime.setText(note.getDateTime());
        }
    }
}
