package com.sun.kikyorss.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(version = 1,entities = [Channel::class])
abstract class ChannelDatabase : RoomDatabase() {
    abstract fun channelDao(): ChannelDao

    companion object {
        private var instance: ChannelDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): ChannelDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                ChannelDatabase::class.java, "channel_database"
            ).allowMainThreadQueries().build().apply { instance = this}
        }
    }
}