package com.example.learningapp.notes.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "notes")
public class Note implements Serializable
{

    //   @PrimaryKey(autoGenerate = true)
    private String id;
    //    @ColumnInfo(name = "title")
    private String title;
    //    @ColumnInfo(name = "date_time")
    private String dateTime;
    //    @ColumnInfo(name = "subtitle")
    private String subTitle;
    //    @ColumnInfo(name = "note_text")
    private String noteText;
    //    @ColumnInfo(name = "image_path")
    private String imagePath;
    //   @ColumnInfo(name = "color")
    private String color;
    //    @ColumnInfo(name = "web_link")
    private String webLink;

    public Note()
    {
    }


    public Note(String id, String title, String dateTime, String subTitle, String noteText, String imagePath, String color, String webLink)
    {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.subTitle = subTitle;
        this.noteText = noteText;
        this.imagePath = imagePath;
        this.color = color;
        this.webLink = webLink;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }

    public String getSubTitle()
    {
        return subTitle;
    }

    public void setSubTitle(String subTitle)
    {
        this.subTitle = subTitle;
    }

    public String getNoteText()
    {
        return noteText;
    }

    public void setNoteText(String noteText)
    {
        this.noteText = noteText;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getWebLink()
    {
        return webLink;
    }

    public void setWebLink(String webLink)
    {
        this.webLink = webLink;
    }

    @NonNull
    @Override
    public String toString()
    {
        return title + ":" + dateTime;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                Objects.equals(title, note.title) &&
                Objects.equals(dateTime, note.dateTime) &&
                Objects.equals(subTitle, note.subTitle) &&
                Objects.equals(noteText, note.noteText) &&
                Objects.equals(imagePath, note.imagePath) &&
                Objects.equals(color, note.color) &&
                Objects.equals(webLink, note.webLink);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, title, dateTime, subTitle, noteText, imagePath, color, webLink);
    }
}
