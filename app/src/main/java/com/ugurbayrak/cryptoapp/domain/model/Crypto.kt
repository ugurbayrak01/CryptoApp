package com.ugurbayrak.cryptoapp.domain.model

data class Crypto(
    val circulatingSupply: String,
    val maxSupply: String,
    val name: String,
    val symbol: String,
    val totalSupply: String,
    val marketCap: String,
    val percentChange1h: String,
    val percentChange24h: String,
    val price: String,
    val volume24h: String,
    val volumeChange24h: String,
    val logoUrl: String,
    val detailLogoUrl: String
)
