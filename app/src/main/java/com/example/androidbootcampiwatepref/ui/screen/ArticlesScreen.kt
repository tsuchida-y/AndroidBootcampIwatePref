package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidbootcampiwatepref.domain.domainobject.Article
import com.example.androidbootcampiwatepref.viewmodel.ArticlesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ArticlesScreen(
    modifier: Modifier = Modifier,
    viewModel: ArticlesViewModel = viewModel(),
) {
    val uiModel by viewModel.uiModel.collectAsState()
    ArticleList(
        articles = uiModel.articles,
        modifier = modifier,
    )
}

@Composable
fun ArticleList(
    articles: List<Article>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            count = articles.size,
            key = { index -> articles[index].id },
            itemContent = { index ->
                ArticleItem(
                    article = articles[index],
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
    }
}

@Composable
fun ArticleItem(
    article: Article,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = article.body,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleListPreview() {
    val articles = listOf(
        Article(
            id = "1",
            title = "Title 1",
            body = "Body 1",
        ),
        Article(
            id = "2",
            title = "Title 2",
            body = "Body 2",
        ),
    )
    ArticleList(
        articles = articles,
        modifier = Modifier.fillMaxSize(),
    )
}