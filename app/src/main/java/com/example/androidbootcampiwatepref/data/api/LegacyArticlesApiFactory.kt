package com.example.androidbootcampiwatepref.data.api

object LegacyArticlesApiFactory {

    private var instance: LegacyArticlesApi? = null

    fun create(): LegacyArticlesApi {
        return instance ?: LegacyArticlesApiImpl(dataSource = ArticleDataSource()).also { instance = it }
    }
}