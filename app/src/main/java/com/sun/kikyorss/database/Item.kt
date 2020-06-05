package com.sun.kikyorss.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    val parent: String,
    val title: String,
    @PrimaryKey val link: String,
    val description: String,
    var pubDate: String = "",
    var lastBuildDate:String="",
    var hadRead: Boolean = false,
    var star: Boolean = false
)