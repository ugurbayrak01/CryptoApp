package com.ugurbayrak.cryptoapp.presentation.cryptolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ugurbayrak.cryptoapp.domain.model.Crypto
import com.ugurbayrak.cryptoapp.domain.repository.CryptoRepository
import com.ugurbayrak.cryptoapp.domain.usecase.GetCryptosFromAPIUseCase
import com.ugurbayrak.cryptoapp.util.CustomSharedPreferences
import com.ugurbayrak.cryptoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val getCryptosFromAPIUseCase: GetCryptosFromAPIUseCase,
    application: Application
) : AndroidViewModel(application) {
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L
    private var initialCryptos = listOf<Crypto>()
    private var isSearchStarting = true

    private val _state = MutableStateFlow(CryptoListState())
    val state: StateFlow<CryptoListState>
        get() = _state

    private fun searchCrypto(search: String) {
        val listToSearch = if(isSearchStarting) {
            _state.value.cryptos
        } else {
            initialCryptos
        }

        viewModelScope.launch(Dispatchers.Default) {
            if(search.isEmpty()) {
                _state.value = CryptoListState(cryptos = initialCryptos)
                isSearchStarting = true
                return@launch
            }

            val result = listToSearch.filter {
                it.symbol.contains(search.trim(), ignoreCase = true)
            }

            if(isSearchStarting) {
                initialCryptos = _state.value.cryptos
                isSearchStarting = false
            }

            _state.value = CryptoListState(cryptos = result)
        }
    }

    fun refreshCryptos() {
        val updateTime = customPreferences.getTime()

        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getCryptosFromSQLite()
        } else {
            getCryptosFromAPI()
        }
    }

    fun getCryptosFromAPI() {
        getCryptosFromAPIUseCase.executeGetCryptosFromAPI().onEach {
            when(it) {
                is Resource.Success -> {
                    storeCryptosInSQLite(it.data ?: emptyList())
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

    private fun storeCryptosInSQLite(cryptos: List<Crypto>) {
        viewModelScope.launch {
            try {
                repository.deleteAllCryptos()

                val listLong = repository.insertCryptos(*cryptos.toTypedArray())
                var i = 0

                while(i < cryptos.size) {
                    cryptos[i].id = listLong[i].toInt()
                    i++
                }
            } catch (e: Exception) {
                _state.value = CryptoListState(error = e.localizedMessage ?: "Error!")
            } finally {
                _state.value = CryptoListState(cryptos = cryptos)
                customPreferences.saveTime(System.nanoTime())
            }
        }
    }

    private fun getCryptosFromSQLite() {
        _state.value = CryptoListState(isLoading = true)

        viewModelScope.launch {
            try {
                repository.getAllCryptos().collect {
                    _state.value = CryptoListState(cryptos = it)
                }
            } catch (e: Exception) {
                _state.value = CryptoListState(error = e.localizedMessage ?: "Error!")
            }
        }
    }

    fun onEvent(event: CryptoListEvent) {
        when(event) {
            is CryptoListEvent.Search -> {
                searchCrypto(event.search)
            }
        }
    }
}