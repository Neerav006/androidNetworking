package com.example.myapplication.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsRoom(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    @ColumnInfo(name = "title")
    val title:String

) {
}