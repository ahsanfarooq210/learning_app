package com.example.learningapp.notes.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.notes.entities.Note;
import com.example.learningapp.notes.listiners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>
{
    private List<Note> notes;
    private NotesListener notesListener;
    private Timer timer;
    private List<Note> noteSource;

    public NotesAdapter(List<Note> notes, NotesListener notesListener)
    {
        this.notes = notes;
        this.notesListener = notesListener;
        noteSource = notes;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new NotesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cotainer_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, final int position)
    {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                notesListener.onNoteClicked(notes.get(position), position);
            }
        });
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
        LinearLayout layoutNote;
        RoundedImageView imageNote;

        public NotesViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textSubTitle = itemView.findViewById(R.id.textSubTiitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
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

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null)
            {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else
            {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
            if (note.getImagePath() != null)
            {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            } else
            {
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    public void searchNotes(final String searchKeyWord)
    {
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (searchKeyWord.trim().isEmpty())
                {
                    notes = noteSource;
                } else
                {
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note : noteSource)
                    {
                        if (note.getTitle().toLowerCase().contains(searchKeyWord.toLowerCase()) || note.getSubTitle().toLowerCase().contains(searchKeyWord.toLowerCase()) || note.getNoteText().toLowerCase().contains(searchKeyWord.toLowerCase()))
                        {
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer()
    {
        if (timer != null)
        {
            timer.cancel();
        }
    }
}
