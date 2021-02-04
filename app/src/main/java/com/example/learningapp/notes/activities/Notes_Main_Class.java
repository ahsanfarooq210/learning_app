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
        getNotes();
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

    private void getNotes()
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
                if (noteList.size() == 0)
                {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else
                {
                    if (!notes.get(0).equals(noteList.get(0)))
                    {
                        noteList.add(0, notes.get(0));
                        notesAdapter.notifyItemInserted(0);
                    }

                }
                notesRecyclerView.smoothScrollToPosition(0);

            }
        }
        new GetNotes().execute();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        getNotes();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK)
        {
            getNotes();
        }
    }
}
