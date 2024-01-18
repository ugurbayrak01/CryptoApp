package com.ugurbayrak.cryptoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ugurbayrak.cryptoapp.domain.model.Crypto
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {
    @Insert
    suspend fun insertCryptos(vararg crypto: Crypto) : List<Long>

    @Query("SELECT * FROM cryptos")
    fun getAllCryptos() : Flow<List<Crypto>>

    @Query("SELECT * FROM cryptos WHERE id = :id")
    suspend fun getCrypto(id: Int) : Crypto

    @Query("DELETE FROM cryptos")
    suspend fun deleteAllCryptos()
}