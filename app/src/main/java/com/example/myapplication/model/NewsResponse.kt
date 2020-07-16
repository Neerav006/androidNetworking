package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

class NewsResponse{
        data class Response(
                @SerializedName("articles")
                val news: List<News>
        )

        data class News(
                val title: String,
                @SerializedName("urlToImage")
                val image: String?
        )
}

