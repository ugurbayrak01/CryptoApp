package com.ugurbayrak.cryptoapp.presentation.cryptodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ugurbayrak.cryptoapp.domain.repository.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoDetailViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel(){
    private val _state = MutableStateFlow(CryptoDetailState())
    val state: StateFlow<CryptoDetailState>
        get() = _state

    fun getCryptoFromSQLite(id: Int) {
        viewModelScope.launch {
            try {
                val crypto = repository.getCrypto(id)
                _state.value = CryptoDetailState(crypto = crypto)
            } catch (e: Exception) {
                _state.value = CryptoDetailState(error = e.localizedMessage ?: "Error!")
            }
        }
    }
}