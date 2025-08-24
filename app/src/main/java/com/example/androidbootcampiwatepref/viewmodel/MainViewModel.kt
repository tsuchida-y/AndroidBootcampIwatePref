package com.example.androidbootcampiwatepref.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampiwatepref.data.store.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    val dataStore: AppDataStore
) : ViewModel() {
    val counts get() = dataStore.counts
    val greetingData: Flow<String?> = dataStore.greetingData

    // AppDataStoreのsetGreetingDataを呼び出す関数
    fun setGreetingData(jsonData: String) {
        viewModelScope.launch {
            dataStore.setGreetingData(jsonData)
        }
    }

    fun setCount(index: Int, countValue: Int) {
        viewModelScope.launch {
            dataStore.setCount(index, countValue)
        }
    }
}