package com.example.androidbootcampiwatepref.data.api

import com.example.androidbootcampiwatepref.domain.domainobject.Article
import kotlinx.coroutines.delay

class ArticlesApiImpl(
    private val dataSource: ArticleDataSource,
) : ArticlesApi{
    override suspend fun getArticles(): List<Article> {
        delay(5_000)
        return dataSource.getArticles()
    }
}