package com.sun.kikyorss.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(version = 3, entities = [Item::class])
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        private var instance: ItemDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): ItemDatabase {
            instance?.let { return it }
            return Room.databaseBuilder(
                context.applicationContext,
                ItemDatabase::class.java,
                "item_database"
            ).allowMainThreadQueries().addMigrations(object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("alter table item_database add pubData String")
                    database.execSQL("alter table item_database add lastBuildDate String")
                    database.execSQL("alter table item_database add hadRead Boolean")
                    database.execSQL("alter table item_database add star Boolean")
                }
            }).addMigrations(object :Migration(2,3){
                override fun migrate(database: SupportSQLiteDatabase) {
                    //database.execSQL("alter table item_database drop column pubData")
                    database.execSQL("alter table item_database add pubDate String")
                }
            }).build()
                .apply {
                    instance = this
                }
        }
    }
}

/*lateinit var pubData: String
    lateinit var lastBuildDate: String
    var hadRead: Boolean = false*/