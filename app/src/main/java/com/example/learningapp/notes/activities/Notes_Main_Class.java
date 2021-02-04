package com.example.learningapp.notes.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.learningapp.Database.Database;
import com.example.learningapp.R;
import com.example.learningapp.notes.adapters.NotesAdapter;
import com.example.learningapp.notes.entities.Note;
import com.example.learningapp.notes.listiners.NotesListener;
import com.google.api.Distribution;

import java.util.ArrayList;
import java.util.List;

public class Notes_Main_Class extends AppCompatActivity implements NotesListener
{
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int REQUEST_CODE_UPDATE_NOTE = 2;
    private static final int REQUESR_CODE_SHOW_NOTE = 3;

    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    private int noteClickedPosition = -1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main_class);
        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE);
            }
        });
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);
        getNotes(REQUESR_CODE_SHOW_NOTE);
    }

    @Override
    public void onNoteClicked(Note note, int position)
    {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);

    }

    private void getNotes(final int requestCode)
    {
        class GetNotes extends AsyncTask<Void, Void, List<Note>>
        {

            @Override
            protected List<Note> doInBackground(Void... voids)
            {
                return Database.Companion.getDatabase(getApplicationContext()).getNotesDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes)
            {
                super.onPostExecute(notes);
                if (requestCode == REQUESR_CODE_SHOW_NOTE)
                {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else
                {
                    if (requestCode == REQUEST_CODE_ADD_NOTE)
                    {
                        noteList.add(0, notes.get(0));
                        notesAdapter.notifyItemInserted(0);
                        notesRecyclerView.smoothScrollToPosition(0);
                    } else
                    {
                        if (requestCode == REQUEST_CODE_UPDATE_NOTE)
                        {
                            noteList.remove(noteClickedPosition);
                            noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                            notesAdapter.notifyItemChanged(noteClickedPosition);
                        }
                    }
                }

            }
        }
        new GetNotes().execute();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        getNotes(REQUESR_CODE_SHOW_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK)
        {
            getNotes(REQUEST_CODE_ADD_NOTE);
        } else
        {
            if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK)
            {
                getNotes(REQUEST_CODE_UPDATE_NOTE);
            }
        }
    }
}
