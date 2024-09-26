package com.example.androidbootcampiwatepref.usecase

import com.example.androidbootcampiwatepref.data.api.ArticlesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetArticlesUseCase(
    private val articlesApi: ArticlesApi,
) {

    private suspend fun execute() = runCatching {
        withContext(Dispatchers.IO) {
            // このブロックないだけIOスレッドで実行される
            articlesApi.getArticles()
        }
    }

    suspend operator fun invoke() = execute()
}