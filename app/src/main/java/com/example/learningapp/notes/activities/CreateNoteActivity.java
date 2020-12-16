package com.example.learningapp.notes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningapp.Database.Database;
import com.example.learningapp.R;
import com.example.learningapp.notes.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity
{

    private EditText inputNoteTitle, inputNoteSubTitle, inputNoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator;

    private String selectedNoteColor;

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
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);

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

        selectedNoteColor = "#333333";

        initMiscellaneous();
        setViewSubtitleIndicateColor();


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
        note.setColor(selectedNoteColor);

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

    private void initMiscellaneous()
    {
        final LinearLayout layoutmiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutmiscellaneous);
        layoutmiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutmiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutmiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutmiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutmiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutmiscellaneous.findViewById(R.id.imageColor5);

        layoutmiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicateColor();
            }
        });

        layoutmiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#FDBE38";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicateColor();
            }
        });
        layoutmiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicateColor();
            }
        });
        layoutmiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#3a52FC";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicateColor();
            }
        });
        layoutmiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                setViewSubtitleIndicateColor();
            }
        });

    }

    private void setViewSubtitleIndicateColor()
    {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }
}