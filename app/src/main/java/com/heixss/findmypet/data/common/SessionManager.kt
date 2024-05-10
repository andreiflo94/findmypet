package com.heixss.findmypet.data.common

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

import javax.inject.Inject

data class SessionManager @Inject constructor(val applicationContext: Context) {

    private var sharedPreferences: SharedPreferences

    init {
        val masterKey: MasterKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            applicationContext,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveAccessToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getAccessToken(): String? = sharedPreferences.getString("token", null)

    fun clearAccessToken() {
        val editor = sharedPreferences.edit()
        editor.putString("token", null)
        editor.apply()
    }

}