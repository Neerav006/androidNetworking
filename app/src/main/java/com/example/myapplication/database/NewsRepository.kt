package com.example.myapplication.database

import com.example.myapplication.model.room.NewsRoom

class NewsRepository(private val newsDao: NewsDao) {

    suspend fun insertAll(newsList:ArrayList<NewsRoom>){
        newsDao.insertNews(newsList)

    }

    suspend fun getAllNews() : List<NewsRoom>{
        return  newsDao.getNewsList()
    }

    suspend fun deleteAllNews(){
         newsDao.deleteAllNews()
    }

}