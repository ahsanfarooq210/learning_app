package com.example.learningapp.notes.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.Database.Database;
import com.example.learningapp.R;
import com.example.learningapp.notes.entities.Note;

import java.util.List;

public class Notes_Main_Class extends AppCompatActivity
{
    public static final int REQUEST_CODE_ADD_NOTE = 1;

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
        getNotes();
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
                Log.d("My_Notes", notes.toString());

            }
        }
        new GetNotes().execute();
    }
}
