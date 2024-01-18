package com.ugurbayrak.cryptoapp.presentation.cryptolist

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ugurbayrak.cryptoapp.domain.model.Crypto
import com.ugurbayrak.cryptoapp.domain.repository.CryptoRepository
import com.ugurbayrak.cryptoapp.domain.usecase.GetCryptosFromAPIUseCase
import com.ugurbayrak.cryptoapp.util.CustomSharedPreferences
import com.ugurbayrak.cryptoapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _state = MutableStateFlow(CryptoListState())
    val state: StateFlow<CryptoListState>
        get() = _state

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
                    Toast.makeText(getApplication(),"Cryptos From API", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(getApplication(),"Cryptos From SQLite", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                _state.value = CryptoListState(error = e.localizedMessage ?: "Error!")
            }
        }
    }
}