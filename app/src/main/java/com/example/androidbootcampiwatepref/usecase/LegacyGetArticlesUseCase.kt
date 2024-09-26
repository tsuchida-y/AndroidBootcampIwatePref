package com.example.androidbootcampiwatepref.usecase

import android.os.AsyncTask
import com.example.androidbootcampiwatepref.data.api.LegacyArticlesApi
import com.example.androidbootcampiwatepref.domain.domainobject.Article

class LegacyGetArticlesUseCase(
    private val articlesApi: LegacyArticlesApi,
) : AsyncTask<Unit, Unit, Result<List<Article>>>() {

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun doInBackground(vararg params: Unit): Result<List<Article>> {
        return try {
            // バックグラウンドのスレッドでAPIリクエスト
            val articles = articlesApi.getArticles().get()
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun onPostExecute(result: Result<List<Article>>) {
        // メインスレッドでコールバックを実行する
        result.fold(
            onSuccess = { articles ->
                callback?.onArticlesLoaded(articles)
            },
            onFailure = {
                callback?.onArticlesLoadFailed()
            }
        )
    }

    interface Callback {
        fun onArticlesLoaded(articles: List<Article>)
        fun onArticlesLoadFailed()
    }

    operator fun invoke() = execute()
}