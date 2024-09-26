package com.example.androidbootcampiwatepref.data.api

import com.example.androidbootcampiwatepref.domain.domainobject.Article
import java.util.concurrent.Future

interface LegacyArticlesApi {

    fun getArticles(): Future<List<Article>>
}