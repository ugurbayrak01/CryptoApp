package com.ugurbayrak.cryptoapp.presentation.cryptodetail

import com.ugurbayrak.cryptoapp.domain.model.Crypto

data class CryptoDetailState (
    val crypto: Crypto? = null,
    val error: String = ""
)