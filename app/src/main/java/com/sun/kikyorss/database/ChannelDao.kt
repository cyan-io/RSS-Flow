package com.sun.kikyorss.database

import androidx.room.*

@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(channel: Channel)

    @Delete
    fun delete(channel: Channel)

    @Update
    fun update(channel: Channel)

    @Query("select * from Channel")
    fun loadAll(): List<Channel>

    @Query("select * from channel where link = :l")
    fun query(l: String): Channel?

    fun isExist(l: String) = query(l) != null

    @Query("select count(*) from channel")
    fun size():Int
}