package com.ugurbayrak.cryptoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
    val cmc_rank: Int,
    val date_added: String,
    val id: Int,
    val infinite_supply: Boolean,
    val last_updated: String,
    @SerializedName("max_supply")
    val maxSupply: Double,
    val name: String,
    val num_market_pairs: Int,
    val platform: Platform,
    val quote: Quote,
    val self_reported_circulating_supply: Double,
    val self_reported_market_cap: Double,
    val slug: String,
    val symbol: String,
    val tags: List<String>,
    @SerializedName("total_supply")
    val totalSupply: Double,
    val tvl_ratio: Double
)