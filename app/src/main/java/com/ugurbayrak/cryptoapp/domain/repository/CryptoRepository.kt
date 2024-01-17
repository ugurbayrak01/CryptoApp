package com.ugurbayrak.cryptoapp.domain.repository

import com.ugurbayrak.cryptoapp.data.remote.dto.CryptoResponse
import retrofit2.Response

interface CryptoRepository {
    suspend fun getCryptos() : Response<CryptoResponse>
}