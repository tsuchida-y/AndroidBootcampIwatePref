package com.example.androidbootcampiwatepref.data.api

object ArticlesApiFactory {

    private var instance: ArticlesApi? = null

    fun create(): ArticlesApi {
        return instance ?: ArticlesApiImpl(dataSource = ArticleDataSource()).also { instance = it }
    }
}