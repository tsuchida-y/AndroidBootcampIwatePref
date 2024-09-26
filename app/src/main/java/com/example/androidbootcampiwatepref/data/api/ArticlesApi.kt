package com.example.androidbootcampiwatepref.data.api

import com.example.androidbootcampiwatepref.domain.domainobject.Article

interface ArticlesApi {

    suspend fun getArticles(): List<Article>
}