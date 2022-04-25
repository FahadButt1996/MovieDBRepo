package com.example.starzplayassignment.utilities

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.*

class LanguageConfigs(val context: Context) {

    init {
        selectedLanguageEnum = language
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    private fun readKey(key: String): String? {
        var value: String? = ""
        runBlocking {
            withContext(Dispatchers.Default) {
                val dataStoreKey = stringPreferencesKey(key)
                val preferences = context.dataStore.data.first()
                value = preferences[dataStoreKey]
            }
        }
        return value
    }

    var language: LanguageEnum
        get() {
            val code = readKey(SELECTED_LANGUAGE_CODE)

            return if (!code.isNullOrEmpty() && code.equals(LanguageEnum.ARABIC.languageCode, true)) {
                LanguageEnum.ARABIC
            } else {
                LanguageEnum.ENGLISH
            }
        }
        set(value) {
            selectedLanguageEnum = value
            GlobalScope.launch {
                save(SELECTED_LANGUAGE_CODE, value.languageCode)
            }
        }

    companion object {
        private const val PREF_KEY = "language_configs"

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = PREF_KEY
        )

        private const val SELECTED_LANGUAGE_CODE = "selectedLanguageCode"
        var selectedLanguageEnum: LanguageEnum = LanguageEnum.ENGLISH

        fun initializeLocale(context: Context) {
            val locale =
                Locale(selectedLanguageEnum.languageCode)
            Locale.setDefault(locale)
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
            context.createConfigurationContext(config)
        }
    }
}