package com.example.tradeapp.damin.modul

import android.content.Context
import android.content.SharedPreferences
import com.example.tradeapp.damin.database.SecureStorage

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.spght.encryptedprefs.EncryptedSharedPreferences
import dev.spght.encryptedprefs.MasterKey

import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ToolsModule {


    @Provides
    @Singleton
    fun provideMasterKey(context: Context): MasterKey{
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(context: Context,masterKey:MasterKey): SharedPreferences{
        return EncryptedSharedPreferences(
            context,
            "auth_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideSecureStorage(context: Context,sharedPreferences:SharedPreferences): SecureStorage{
        return SecureStorage(context,sharedPreferences)
    }
}