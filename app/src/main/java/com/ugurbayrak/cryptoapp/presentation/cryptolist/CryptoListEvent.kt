package com.ugurbayrak.cryptoapp.presentation.cryptolist

sealed class CryptoListEvent {
    data class Search(val search: String) : CryptoListEvent()
}