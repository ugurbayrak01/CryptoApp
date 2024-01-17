package com.ugurbayrak.cryptoapp.presentation.cryptolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ugurbayrak.cryptoapp.domain.usecase.GetCryptosUseCase
import com.ugurbayrak.cryptoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val getCryptosUseCase: GetCryptosUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CryptoListState())
    val state: StateFlow<CryptoListState>
        get() = _state

    fun getCryptos() {
        getCryptosUseCase.executeGetCryptos().onEach {
            when(it) {
                is Resource.Success -> {
                    _state.value = CryptoListState(cryptos = it.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = CryptoListState(error = it.message ?: "Error!")
                }
                is Resource.Loading -> {
                    _state.value = CryptoListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}