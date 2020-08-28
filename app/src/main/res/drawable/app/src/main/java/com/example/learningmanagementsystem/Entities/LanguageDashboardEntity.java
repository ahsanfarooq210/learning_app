package com.example.learningmanagementsystem.Entities;

import android.net.Uri;

public class LanguageDashboardEntity
{
    private String name,discription;
    private int imaage;

    public LanguageDashboardEntity()
    {
    }

    public LanguageDashboardEntity(String name, String discription, int imaage)
    {
        this.name = name;
        this.discription = discription;
        this.imaage = imaage;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDiscription()
    {
        return discription;
    }

    public void setDiscription(String discription)
    {
        this.discription = discription;
    }

    public int getImaage()
    {
        return imaage;
    }

    public void setImaage(int imaage)
    {
        this.imaage = imaage;
    }
}
