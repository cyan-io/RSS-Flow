package com.sun.kikyorss.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Channel(
    var title: String,
    @PrimaryKey val link: String,
    val description: String
)