package com.example.learningapp.notes.listiners;

import com.example.learningapp.notes.entities.Note;

public interface NotesListener
{
    void onNoteClicked(Note note, int position);
}
