package com.ugurbayrak.cryptoapp.domain.usecase

import com.ugurbayrak.cryptoapp.data.util.toCrypto
import com.ugurbayrak.cryptoapp.domain.model.Crypto
import com.ugurbayrak.cryptoapp.domain.repository.CryptoRepository
import com.ugurbayrak.cryptoapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCryptosUseCase @Inject constructor(private val repository: CryptoRepository) {

    fun executeGetCryptos() : Flow<Resource<List<Crypto>>> = flow {
        try {
            emit(Resource.Loading())
            val cryptoList = repository.getCryptos()

            if(cryptoList.isSuccessful) {
                cryptoList.body()?.let {
                    emit(Resource.Success(it.toCrypto()))
                } ?: emit(Resource.Error("No data!"))
            } else {
                emit(Resource.Error("No data!"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error!"))
        }
    }
}