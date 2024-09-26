package com.example.androidbootcampiwatepref.data.api

import com.example.androidbootcampiwatepref.domain.domainobject.Article
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class LegacyArticlesApiImpl(
    private val dataSource: ArticleDataSource,
    private val service: ExecutorService = Executors.newSingleThreadExecutor(),
) : LegacyArticlesApi {

    override fun getArticles(): Future<List<Article>> {
        return service.submit(
            Callable {
                try {
                    Thread.sleep(5_000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                dataSource.getArticles()
            }
        )
    }
}