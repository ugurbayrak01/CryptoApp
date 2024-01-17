package com.ugurbayrak.cryptoapp.data.remote

import com.ugurbayrak.cryptoapp.data.remote.dto.CryptoResponse
import com.ugurbayrak.cryptoapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoAPI {
    @GET("/v1/cryptocurrency/listings/latest")
    suspend fun getCryptos(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 200,
        @Query("convert") convert: String = "USD",
        @Query("CMC_PRO_API_KEY") apiKey: String = API_KEY
    ) : Response<CryptoResponse>
}