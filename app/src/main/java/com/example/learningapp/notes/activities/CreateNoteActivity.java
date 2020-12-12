package com.example.learningapp.notes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningapp.Database.Database;
import com.example.learningapp.R;
import com.example.learningapp.notes.entities.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity
{

    private EditText inputNoteTitle, inputNoteSubTitle, inputNoteText;
    private TextView textDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubTitle = findViewById(R.id.inputNoteSubTitle);
        inputNoteText = findViewById(R.id.inputNote);

        textDateTime = findViewById(R.id.textDateTime);
        textDateTime.setText(new SimpleDateFormat("EEEE,dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveNote();
            }
        });


    }

    private void saveNote()
    {
        if (inputNoteTitle.getText().toString().trim().isEmpty())
        {
            //TODO:make a snak bar here
            //TODO:make a yoyo animation
            Toast.makeText(this, "please write the title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (inputNoteSubTitle.getText().toString().trim().isEmpty())
        {
            //TODO:make a snak bar here
            //TODO:make a yoyo animation
            Toast.makeText(this, "please write the subtitle", Toast.LENGTH_SHORT).show();
            return;
        }
        if (inputNoteText.getText().toString().trim().isEmpty())
        {
            //TODO:make a snak bar here
            //TODO:make a yoyo animation
            Toast.makeText(this, "please write the title", Toast.LENGTH_SHORT).show();
            return;
        }

        final Note note = new Note();
        note.setTitle(inputNoteTitle.getText().toString().trim());
        note.setSubTitle(inputNoteSubTitle.getText().toString().trim());
        note.setNoteText(inputNoteText.getText().toString().trim());
        note.setDateTime(textDateTime.getText().toString().trim());

        class SaveNoteTask extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids)
            {
                Database.Companion.getDatabase(getApplicationContext()).getNotesDao().insertNote(note);
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveNoteTask().execute();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        saveNote();
    }
}