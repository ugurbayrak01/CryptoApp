package com.ugurbayrak.cryptoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ugurbayrak.cryptoapp.domain.model.Crypto

@Database(entities = [Crypto::class], version = 1)
abstract class CryptoDatabase : RoomDatabase() {
    abstract fun cryptoDao() : CryptoDao
}