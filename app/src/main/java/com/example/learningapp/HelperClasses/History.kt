package com.example.learningapp.HelperClasses

import android.content.Context
import com.example.learningapp.Database.Database
import com.example.learningapp.Entity.HistoryEntity

class History
{
    public fun savehistory(history: HistoryEntity, context: Context)
    {
        Database.getDatabase(context).gethistoryDao().addHistory(history)

    }
}