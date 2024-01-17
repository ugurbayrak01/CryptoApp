package com.ugurbayrak.cryptoapp.presentation.cryptolist

import com.ugurbayrak.cryptoapp.domain.model.Crypto

data class CryptoListState (
    val isLoading: Boolean = false,
    val cryptos: List<Crypto> = emptyList(),
    val error: String = ""
)