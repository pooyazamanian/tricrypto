package com.example.tradeapp.damin.database
import android.content.Context
import android.content.SharedPreferences
import dev.spght.encryptedprefs.EncryptedSharedPreferences
import dev.spght.encryptedprefs.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit
import javax.inject.Inject

class SecureStorage @Inject constructor(
    val context: Context,
    val sharedPreferences:SharedPreferences
) {
//    private val sharedPreferences = EncryptedSharedPreferences.create(
//        context,
//        "auth_prefs",
//        MasterKey.Builder(context)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build(),
//        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//    )

    suspend fun saveString(key: String, token: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit { putString(key, token) }
    }

    suspend fun getString(key: String,): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(key, null)
    }

    suspend fun clearString(key: String,) = withContext(Dispatchers.IO) {
        sharedPreferences.edit { remove(key) }
    }
}