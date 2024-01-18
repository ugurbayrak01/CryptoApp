package com.ugurbayrak.cryptoapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cryptos")
data class Crypto(
    val circulatingSupply: String,
    val maxSupply: String,
    val name: String,
    val symbol: String,
    val totalSupply: String,
    val marketCap: String,
    val percentChange1h: String,
    val percentChange1hColor: Int,
    val percentChange24h: String,
    val percentChange24hColor: Int,
    val price: String,
    val volume24h: String,
    val volumeChange24h: String,
    val logoUrl: String,
    val detailLogoUrl: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)
