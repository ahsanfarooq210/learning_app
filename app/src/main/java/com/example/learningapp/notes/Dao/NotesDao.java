package com.example.learningapp.notes.Dao;

import android.content.LocusId;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.learningapp.notes.entities.Note;

import java.util.List;

@Dao
public interface NotesDao
{
    @Query("select *from notes order by id DESC")
    List<Note> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);


}
