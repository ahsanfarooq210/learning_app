package com.example.learningapp.HelperClasses

import android.content.Context
import android.provider.ContactsContract
import com.example.learningapp.Database.Database
import com.example.learningapp.Entity.HistoryEntity

class History
{
    fun savehistory(history: HistoryEntity, context: Context)
    {
        Database.getDatabase(context).gethistoryDao().addHistory(history)

    }

    fun getAllHisotry(context: Context): List<HistoryEntity>
    {
        return Database.getDatabase(context).gethistoryDao().getAllHistory()
    }

    fun getUrl(context: Context): List<String>
    {
        return Database.getDatabase(context).gethistoryDao().getAllurl()
    }

    fun historyClear(context: Context)
    {
        return Database.getDatabase(context).gethistoryDao().deleteAllHistory()
    }


}