package com.ugurbayrak.cryptoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class USD(
    val fully_diluted_market_cap: Double,
    val last_updated: String,
    @SerializedName("market_cap")
    val marketCap: Double,
    val market_cap_dominance: Double,
    @SerializedName("percent_change_1h")
    val percentChange1h: Double,
    @SerializedName("percent_change_24h")
    val percentChange24h: Double,
    val percent_change_30d: Double,
    val percent_change_60d: Double,
    val percent_change_7d: Double,
    val percent_change_90d: Double,
    val price: Double,
    val tvl: Double,
    @SerializedName("volume_24h")
    val volume24h: Double,
    @SerializedName("volume_change_24h")
    val volumeChange24h: Double
)