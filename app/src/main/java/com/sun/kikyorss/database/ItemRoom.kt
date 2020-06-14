package com.sun.kikyorss.database

import android.content.Context
import androidx.annotation.Nullable
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Entity
data class Item(
    val parent: String,
    val title: String,
    @PrimaryKey val link: String,
    val description: String,
    var pubDate: String = "",
    var lastBuildDate: String = "",
    var hadRead: Boolean = false,
    var star: Boolean = false
)


@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Delete
    fun delete(item: Item)

    @Update
    fun update(item: Item)

    @Query("delete from Item")
    fun clear()

    @Query("select * from Item where parent = :p")
    fun load(p: String): List<Item>

    @Query("delete from Item where parent = :link")
    fun deleteByChannel(link: String)

    @Query("select * from Item")
    fun loadAll(): List<Item>

    @Query("select * from Item where link= :link")
    fun query(link: String): Item?

    fun compareInsertDelete(itemList: MutableList<Item>) {
        for (i in itemList) {
            i.hadRead = query(i.link)?.hadRead ?: false
        }
        if (itemList.size > 0)
            deleteByChannel(itemList.last().parent)
        for (i in itemList)
            insert(i)
    }
}

@Database(version = 1, entities = [Item::class])
abstract class ItemDatabase : RoomDatabase() {
    abstract fun getItemDao(): ItemDao

    companion object {
        private var instance: ItemDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): ItemDatabase {
            instance?.let { return it }
            return Room.databaseBuilder(
                context.applicationContext,
                ItemDatabase::class.java,
                "Items"
            ).build()
                .apply {
                    instance = this
                }
        }
    }
}