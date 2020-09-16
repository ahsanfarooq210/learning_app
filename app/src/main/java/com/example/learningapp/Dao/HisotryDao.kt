package com.example.learningapp.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.learningapp.Entity.HistoryEntity

@Dao
interface HisotryDao
{
    @Insert
    fun addHistory(history: HistoryEntity)

    @Delete
    fun deleteHistory(history: HistoryEntity)

    @Update
    fun updateHistory(history: HistoryEntity)


}