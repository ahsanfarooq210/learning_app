package com.example.learningapp.Entity;

public class SavedPages
{
    private String id;
    private String title;
    private String url;

    public SavedPages()
    {
    }

    public SavedPages(String id, String title, String url)
    {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public SavedPages(String title, String url)
    {
        this.title = title;
        this.url = url;
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

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
