package com.ugurbayrak.cryptoapp.data.remote.dto

data class CryptoResponse(
    val `data`: List<Data>,
    val status: Status
)