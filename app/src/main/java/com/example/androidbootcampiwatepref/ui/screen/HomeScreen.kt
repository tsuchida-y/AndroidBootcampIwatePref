package com.example.androidbootcampiwatepref.ui.screen

import com.example.androidbootcampiwatepref.viewmodel.MainViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidbootcampiwatepref.viewmodel.GreetingViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: GreetingViewModel = com.example.androidbootcampiwatepref.viewmodel.GreetingViewModel(),
    mainViewModel: MainViewModel,
) {
    // 初回ロード時のデータ読み込みや終了時の保存を行います
    LaunchedEffect(Unit) {
        mainViewModel.greetingData.collect { jsonData ->
            if (!jsonData.isNullOrBlank())
                viewModel.deserializeGreetings(jsonData)
            // カウントの初期化
            mainViewModel.counts.collect { counts ->
                viewModel.initializeCounts(counts)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            mainViewModel.setGreetingData(viewModel.serializeGreetings())
        }
    }
    // ナビゲーションの例として Home と Detail の 2 画面を管理
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            GreetingScreen(navController, viewModel, mainViewModel)
        }
        composable("detail") {
            DetailScreen(navController, viewModel, mainViewModel)
        }
    }
}

@Composable
fun GreetingScreen(
    navController: NavHostController,
    viewModel: GreetingViewModel,
    mainViewModel: MainViewModel,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("detail") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(bottom = 64.dp)
            ) {
                Text(
                    text = "自己紹介の項目",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                GreetingList(viewModel, mainViewModel)
            }
        }
    )
}

@Composable
fun GreetingList(
    viewModel: GreetingViewModel,
    mainViewModel: MainViewModel,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        items(viewModel.greetings) { greeting ->
            // 各項目のレイアウト
            androidx.compose.foundation.layout.Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${greeting.text} (${greeting.count})",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                )
                IconButton(onClick = {
                    val index = viewModel.greetings.indexOf(greeting)
                    if (index != -1) {
                        viewModel.incrementCount(index)
                        mainViewModel.setCount(index, viewModel.greetings[index].count)
                    }
                }) {
                    Icon(Icons.Filled.ThumbUp, contentDescription = "Like")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    viewModel: GreetingViewModel,
    mainViewModel: MainViewModel,
) {
    var newGreeting by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("追加画面") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                TextField(
                    value = newGreeting,
                    onValueChange = { newGreeting = it },
                    label = { Text("新しい項目") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (newGreeting.isNotBlank()) {
                            viewModel.addGreeting(newGreeting, mainViewModel)
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("追加")
                }
            }
        }
    )
}