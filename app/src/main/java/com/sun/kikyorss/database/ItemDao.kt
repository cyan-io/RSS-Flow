package com.sun.kikyorss.database

import androidx.room.*

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
        for(i in itemList)
            insert(i)
    }
}