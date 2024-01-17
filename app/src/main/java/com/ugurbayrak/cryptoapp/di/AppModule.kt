package com.ugurbayrak.cryptoapp.di


import com.ugurbayrak.cryptoapp.data.remote.CryptoAPI
import com.ugurbayrak.cryptoapp.data.repository.CryptoRepositoryImpl
import com.ugurbayrak.cryptoapp.domain.repository.CryptoRepository
import com.ugurbayrak.cryptoapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCryptoAPI() : CryptoAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideCryptoRepository(cryptoAPI: CryptoAPI) : CryptoRepository {
        return CryptoRepositoryImpl(cryptoAPI)
    }
}