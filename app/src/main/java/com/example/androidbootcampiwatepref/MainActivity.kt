package com.example.androidbootcampiwatepref

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.androidbootcampiwatepref.data.store.AppDataStore
import com.example.androidbootcampiwatepref.ui.screen.HomeScreen
import com.example.androidbootcampiwatepref.ui.theme.AndroidBootcampIwatePrefTheme
import com.example.androidbootcampiwatepref.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore = AppDataStore(applicationContext)
        val mainViewModel = MainViewModel(dataStore)
        setContent {
            AndroidBootcampIwatePrefTheme {
                // 例として Greeting 関連の画面 (HomeScreen) を表示
                HomeScreen(mainViewModel = mainViewModel)
            }
        }
    }
}