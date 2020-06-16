package com.sun.kikyorss.database

import android.content.Context
import androidx.room.*


@Entity
data class Item(
    val parent: String,
    val title: String,
    @PrimaryKey val link: String,
    val description: String,
    val pubDate: String?,
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

    @Query("select * from Item where parent = :url")
    fun loadByParent(url: String): MutableList<Item>

    @Query("delete from Item where parent = :url")
    fun deleteByParent(url: String)

    @Query("select * from Item where link= :link")
    fun query(link: String): Item?

    fun compareInsertDelete(itemList: MutableList<Item>) {
        for (i in itemList)
            query(i.link)?.let {
                i.hadRead = it.hadRead
            }
        if (itemList.size > 0)
            deleteByParent(itemList.last().parent)
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
                "Items.db"
            ).allowMainThreadQueries().build().apply { instance = this }
        }
    }
}