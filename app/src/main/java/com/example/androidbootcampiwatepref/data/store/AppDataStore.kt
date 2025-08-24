package com.example.androidbootcampiwatepref.data.store

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//アプリのデータを永続化するためのクラス
//context:アプリがシステムと連携したり、リソースにアクセスしたりするための情報源
class AppDataStore(private val context: Context) {

    //インスタンスを作成しなくても使用できる
    //定数やファクトリメソッドを定義するのに便利
    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
        private const val MAX_ITEMS = 20
        val COUNT_KEYS = List(MAX_ITEMS) { index -> intPreferencesKey("count_$index") }
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    //DataStoreからFlowを使ってデータを非同期に取得
    val counts: Flow<List<Int>> = context.dataStore.data.map { preferences ->
        List(COUNT_KEYS.size) { index ->
            preferences[COUNT_KEYS[index]] ?: 0
        }
    }

    suspend fun setCount(index: Int, count: Int) {
        context.dataStore.edit { preferences ->
            preferences[COUNT_KEYS[index]] = count
        }
    }

    suspend fun setGreetingData(jsonString: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("greetings_data")] = jsonString
        }
    }

    val greetingData: Flow<String?> = context.dataStore.data.map { preferences ->
        val json = preferences[stringPreferencesKey("greetings_data")]
        Log.d("AppDataStore", "Loaded greeting data: $json")
        json
    }
}