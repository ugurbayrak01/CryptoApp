package com.ugurbayrak.cryptoapp.data.repository

import com.ugurbayrak.cryptoapp.data.local.CryptoDao
import com.ugurbayrak.cryptoapp.data.remote.CryptoAPI
import com.ugurbayrak.cryptoapp.data.remote.dto.CryptoResponse
import com.ugurbayrak.cryptoapp.domain.model.Crypto
import com.ugurbayrak.cryptoapp.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class CryptoRepositoryImpl  @Inject constructor(
    private val cryptoAPI: CryptoAPI,
    private val cryptoDao: CryptoDao
) : CryptoRepository {
    override suspend fun getCryptos(): Response<CryptoResponse> {
        return cryptoAPI.getCryptos()
    }

    override suspend fun insertCryptos(vararg crypto: Crypto): List<Long> {
        return cryptoDao.insertCryptos(*crypto)
    }

    override fun getAllCryptos(): Flow<List<Crypto>> {
        return cryptoDao.getAllCryptos()
    }

    override suspend fun getCrypto(id: Int): Crypto {
        return cryptoDao.getCrypto(id)
    }

    override suspend fun deleteAllCryptos() {
        cryptoDao.deleteAllCryptos()
    }
}