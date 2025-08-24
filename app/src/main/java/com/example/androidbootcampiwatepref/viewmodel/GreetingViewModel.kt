package com.example.androidbootcampiwatepref.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class Greeting(
    val text: String,
    var count: Int = 0,
)

class GreetingViewModel : ViewModel() {
    val greetings = mutableStateListOf<Greeting>()

    init {
        if (greetings.isEmpty()) {
            greetings.addAll(
                listOf(
                    Greeting("名前"),
                    Greeting("出身地"),
                    Greeting("職業/学業"),
                    Greeting("趣味"),
                    Greeting("好きな食べ物"),
                    Greeting("好きなスポーツ"),
                    Greeting("好きな国"),
                    Greeting("将来の目標"),
                    Greeting("興味のある分野"),
                    Greeting("今取り組んでいること"),
                    Greeting("好きな映画"),
                    Greeting("好きな音楽"),
                    Greeting("好きな場所"),
                    Greeting("得意なこと")
                )
            )
        }
    }

    fun serializeGreetings(): String {
        return Json.encodeToString(greetings.map { it.copy() })
    }

    fun deserializeGreetings(jsonString: String) {
        try {
            val list = Json.decodeFromString<List<Greeting>>(jsonString)
            list.forEach { greeting ->
                if (greetings.none { it.text == greeting.text }) {
                    greetings.add(greeting)
                }
            }
        } catch (e: Exception) {
            Log.e("GreetingViewModel", "JSON デシリアライズに失敗", e)
        }
    }

    fun addGreeting(newText: String, mainViewModel: MainViewModel) {
        val newGreeting = Greeting(newText)
        greetings.add(newGreeting)
        mainViewModel.setGreetingData(serializeGreetings())
    }

    fun initializeCounts(counts: List<Int>) {
        for (i in greetings.indices) {
            if (i < counts.size) {
                greetings[i].count = counts[i]
            }
        }
    }

    fun incrementCount(index: Int) {
        if (index in greetings.indices) {
            greetings[index] = greetings[index].copy(count = greetings[index].count + 1)
        }
    }
}