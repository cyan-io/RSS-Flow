package com.sun.kikyorss.database

import android.content.Context
import androidx.room.*


@Entity
data class Channel(
    @PrimaryKey val url: String,
    var title: String,
    val link: String,
    val description: String,
    val pubDate:String?,
    val lastBuildDate:String?
)

@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(channel: Channel)

    @Delete
    fun delete(channel: Channel)

    @Update
    fun update(channel: Channel)

    @Query("select * from Channel")
    fun loadAll(): MutableList<Channel>

    @Query("select * from channel where url = :l")
    fun query(l: String): Channel?

    fun isExist(l: String) = query(l) != null

    @Query("select count(*) from channel")
    fun size(): Int
}

@Database(version = 1, entities = [Channel::class])
abstract class ChannelDatabase : RoomDatabase() {
    abstract fun getChannelDao(): ChannelDao

    companion object {
        private var instance: ChannelDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): ChannelDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                ChannelDatabase::class.java, "Channels.db"
            ).allowMainThreadQueries().build().apply { instance = this }
        }
    }
}