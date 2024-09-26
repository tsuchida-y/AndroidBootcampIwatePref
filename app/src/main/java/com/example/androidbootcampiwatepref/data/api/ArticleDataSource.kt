package com.example.androidbootcampiwatepref.data.api

import com.example.androidbootcampiwatepref.domain.domainobject.Article

class ArticleDataSource {

    fun getArticles(): List<Article> {
        return listOf(
            Article(
                id = "1",
                title = "Article 1",
                body = "Article 1 body",
            ),
            Article(
                id = "2",
                title = "Article 2",
                body = "Article 2 body",
            ),
            Article(
                id = "3",
                title = "Article 3",
                body = "Article 3 body",
            ),
        )
    }
}