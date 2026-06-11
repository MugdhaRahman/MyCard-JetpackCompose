package com.mrapps.mycard.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CardEntity::class, AccountEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun accountDao(): AccountDao
}
