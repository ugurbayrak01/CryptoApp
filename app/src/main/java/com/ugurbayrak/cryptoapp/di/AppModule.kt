package com.ugurbayrak.cryptoapp.di


import android.content.Context
import androidx.room.Room
import com.ugurbayrak.cryptoapp.data.local.CryptoDao
import com.ugurbayrak.cryptoapp.data.local.CryptoDatabase
import com.ugurbayrak.cryptoapp.data.remote.CryptoAPI
import com.ugurbayrak.cryptoapp.data.repository.CryptoRepositoryImpl
import com.ugurbayrak.cryptoapp.domain.repository.CryptoRepository
import com.ugurbayrak.cryptoapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideCryptoRepository(cryptoAPI: CryptoAPI, cryptoDao: CryptoDao) : CryptoRepository {
        return CryptoRepositoryImpl(cryptoAPI, cryptoDao)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
            context,
            CryptoDatabase::class.java,
            "CryptoAppDB"
        ).build()


    @Singleton
    @Provides
    fun provideDao(database: CryptoDatabase) = database.cryptoDao()

}