package com.example.learningapp.Dao

import androidx.room.*
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

    @Query("select * from history")
    fun getAllHistory(): List<HistoryEntity>

    @Query("select url from history")
    fun getAllurl(): List<String>

}