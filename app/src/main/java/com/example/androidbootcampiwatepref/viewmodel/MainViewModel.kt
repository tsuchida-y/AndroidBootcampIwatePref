package com.example.androidbootcampiwatepref.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampiwatepref.data.store.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    //コンストラクタでAppDataStoreのインスタンスを受け取る
    val dataStore: AppDataStore

    //ViewModelはUI関連のデータを管理し、UIコンポーネントのライフサイクルに対応
) : ViewModel() {

    //以下の二つの変数の中身は自動で変更される
    val counts get() = dataStore.counts
    val greetingData: Flow<String?> = dataStore.greetingData

    // AppDataStoreに挨拶データ（JSON文字列）を非同期で保存する関数
    fun setGreetingData(jsonData: String) {
        viewModelScope.launch {
            dataStore.setGreetingData(jsonData)
        }
    }

    // 指定したインデックスのカウント値を更新する関数
    fun setCount(index: Int, countValue: Int) {
        viewModelScope.launch {
            dataStore.setCount(index, countValue)
        }
    }
}