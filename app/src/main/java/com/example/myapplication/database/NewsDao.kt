package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.room.NewsRoom

@Dao
interface NewsDao {

    @Query("SELECT * from news ORDER BY title ASC")
    fun getNewsList(): List<NewsRoom>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNews(newsList:ArrayList<NewsRoom>)

    @Query("DELETE FROM news")
    fun deleteAllNews()
}