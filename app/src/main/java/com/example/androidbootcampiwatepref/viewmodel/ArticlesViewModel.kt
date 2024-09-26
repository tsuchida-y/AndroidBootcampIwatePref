package com.example.androidbootcampiwatepref.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampiwatepref.data.api.ArticlesApiFactory
import com.example.androidbootcampiwatepref.domain.domainobject.Article
import com.example.androidbootcampiwatepref.ui.uimodel.ArticlesUiModel
import com.example.androidbootcampiwatepref.usecase.GetArticlesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArticlesViewModel : ViewModel() {

    private val getArticles: GetArticlesUseCase by lazy {
        GetArticlesUseCase(
            articlesApi = ArticlesApiFactory.create()
        )
    }

    private val articles = MutableStateFlow(emptyList<Article>())

    val uiModel: StateFlow<ArticlesUiModel> = articles
        .map { articles -> ArticlesUiModel(articles) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArticlesUiModel(),
        )

    init {
        initScreen()
    }

    private fun initScreen() {
        viewModelScope.launch {
            getArticles().fold(
                onSuccess = { articles ->
                    this@ArticlesViewModel.articles.value = articles
                },
                onFailure = {
                    // ここでエラーハンドリングが可能
                }
            )
        }
    }
}