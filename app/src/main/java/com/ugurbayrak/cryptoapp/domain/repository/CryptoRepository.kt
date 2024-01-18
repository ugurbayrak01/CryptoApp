package com.ugurbayrak.cryptoapp.domain.repository

import com.ugurbayrak.cryptoapp.data.remote.dto.CryptoResponse
import com.ugurbayrak.cryptoapp.domain.model.Crypto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CryptoRepository {
    suspend fun getCryptos() : Response<CryptoResponse>
    suspend fun insertCryptos(vararg crypto: Crypto) : List<Long>
    fun getAllCryptos() : Flow<List<Crypto>>
    suspend fun getCrypto(id: Int) : Crypto
    suspend fun deleteAllCryptos()
}