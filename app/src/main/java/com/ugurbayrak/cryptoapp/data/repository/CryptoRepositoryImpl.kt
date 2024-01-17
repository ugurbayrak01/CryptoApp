package com.ugurbayrak.cryptoapp.data.repository

import com.ugurbayrak.cryptoapp.data.remote.CryptoAPI
import com.ugurbayrak.cryptoapp.data.remote.dto.CryptoResponse
import com.ugurbayrak.cryptoapp.domain.repository.CryptoRepository
import retrofit2.Response
import javax.inject.Inject

class CryptoRepositoryImpl  @Inject constructor(
    private val cryptoAPI: CryptoAPI
) : CryptoRepository {
    override suspend fun getCryptos(): Response<CryptoResponse> {
        return cryptoAPI.getCryptos()
    }
}