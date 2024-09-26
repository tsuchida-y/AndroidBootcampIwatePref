package com.example.androidbootcampiwatepref


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidbootcampiwatepref.ui.theme.AndroidBootcampIwatePrefTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Greeting(
    val text: String,
    var count: Int = 0//再描画
)


class GreetingViewModel : ViewModel() { //ViewModelクラスを継承している
    //配列を作成し、mutableStateListOfで監視している
    val greetings = mutableStateListOf<Greeting>()

    // 例: 初期値を設定
    init {
        greetings.addAll(listOf(
            Greeting("名前"),
            Greeting("出身地"),
            Greeting("職業/学業"),
            Greeting("趣味"),
            Greeting("好きな食べ物"),
            Greeting("好きなスポーツ"),
            Greeting("好きな国"),
            Greeting("将来の目標"),
            Greeting("興味のある分野"),
            Greeting("今取り組んでいること"),
            Greeting("好きな映画"),
            Greeting("好きな音楽"),
            Greeting("好きな場所"),
            Greeting("得意なこと"),
        ))
    }

    //追加
    fun serializeGreetings(): String {
        return Json.encodeToString(greetings.map { it.copy() }) // copyメソッドを使って状態を保持
    }


// デシリアライズ
fun deserializeGreetings(jsonString: String) {
    try {
        val deserializedList = Json.decodeFromString<List<Greeting>>(jsonString)//JSON文字列をGreetingのリストに変換
        greetings.addAll(deserializedList)//greetingsリストに追加
    } catch (e: Exception) {//tryでエラーが発生したらここに来る
    }
}

    //新しい自己紹介項目をリストに追加するメソッド
    fun addGreeting(newText: String,mainViewModel: MainViewModel) {
        val newGreeting = Greeting(newText)
        greetings.add(newGreeting) // リストをクリアせずに新しい項目を追加

        val jsonData = serializeGreetings()
        Log.d("GreetingViewModel", "Serialized greetings: $jsonData")
        mainViewModel.setGreetingData(jsonData) // データストアへ同期
    }

    fun initializeCounts(counts: List<Int>) {
        for (i in greetings.indices) {
            greetings[i].count = counts[i]
        }
    }

    }

class AppDataStore(//DataStoreの実装
    private val context: Context
) {
    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
        private const val MAX_ITEMS = 14 // 項目の数
        val COUNT_KEYS = List(MAX_ITEMS) { index -> intPreferencesKey("count_$index") }
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    // PreferenceDataStoreからFlowでcountを取得するためのプロパティ
    val counts: Flow<List<Int>> // 'count' -> 'counts' に修正
        get() = context.dataStore.data.map { preferences ->
            List(COUNT_KEYS.size) { index ->
                preferences[COUNT_KEYS[index]] ?: 0
            }
        }

    // PreferenceDataStoreにcountを書き込む処理
    suspend fun setCount(index: Int, count: Int) {
        context.dataStore.edit { preferences ->
            preferences[COUNT_KEYS[index]] = count
        }
    }

    //AppDataStore の setGreetingData メソッドを追加します。
    suspend fun setGreetingData(jsonString: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("greetings_data")] = jsonString
        }
    }

    //AppDataStore に greetingData フローを追加します。
    val greetingData: Flow<String?> = context.dataStore.data.map { preferences ->
        //preferences[stringPreferencesKey("greetings_data")]
        val jsonString = preferences[stringPreferencesKey("greetings_data")]
        Log.d("AppDataStore", "Loaded greeting data: $jsonString")
        jsonString
    }

}


class MainViewModel(//ViewModelを介してComposableで扱うことができる
    private val dataStore: AppDataStore
) : ViewModel() {
    val counts: Flow<List<Int>> get() = dataStore.counts // 'count' -> 'counts' に修正
    val greetingData: Flow<String?> get() = dataStore.greetingData // ここを追加
    fun setCount(index: Int, count: Int) {
        viewModelScope.launch {
            dataStore.setCount(index, count)
        }
    }
    fun setGreetingData(jsonString: String) {
        viewModelScope.launch {
            dataStore.setGreetingData(jsonString)
        }
    }
}


class MainActivity : ComponentActivity() {//ComponentActivityクラスを継承している
override fun onCreate(savedInstanceState: Bundle?) {//ここからUIの設定
    super.onCreate(savedInstanceState)
    // ContextからAppDataStoreを生成
    val dataStore = AppDataStore(applicationContext)

    // MainViewModelを生成
    val mainViewModel = MainViewModel(dataStore)
    setContent {
        AndroidBootcampIwatePrefTheme {
            MyApp(mainViewModel = mainViewModel)
        }
    }
}
}



@Composable
fun MyApp(navController: NavHostController = rememberNavController(),
          viewModel: GreetingViewModel = viewModel(),
          mainViewModel: MainViewModel) {
    // GreetingViewModel の状態を remember で保持
    val greetingViewModel: GreetingViewModel = viewModel() // Composable 関数の外で保持

    NavHost(//ここを使ってナビゲーションを管理する(home画面とdetail画面)
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController, viewModel, mainViewModel) }
        composable("detail") { DetailScreen(navController, viewModel, mainViewModel) }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, viewModel: GreetingViewModel,mainViewModel: MainViewModel) {
    val greetings = viewModel.greetings // ここでViewModelの監視を行う
    val counts by mainViewModel.counts.collectAsState(initial = List(viewModel.greetings.size) { 0 })//再描画するタイミングを管理します。リストの初期化

    // 初回ロード時にデータを読み込む
    LaunchedEffect(Unit) {//Composableが初めて表示されるときに実行されるコルーチンを開始します。
        mainViewModel.greetingData.collect { jsonData: String? ->
            // 取得したデータがnullまたは空でない場合
            if (!jsonData.isNullOrBlank()) {
                try {
                    // JSONデータをデシリアライズして、greetingsに設定
                    viewModel.deserializeGreetings(jsonData)
                    Log.d("HomeScreen", "Deserialized successfully")
                } catch (e: Exception) {
                    // デシリアライズエラーをログに出力
                    Log.e("HomeScreen", "Deserialization error: ${e.message}")
                }
            } else if (viewModel.greetings.isEmpty()) { // 初期データがない場合のみ追加
                Log.d("HomeScreen", "Adding initial greetingsあ")
                // 初期データをgreetingsに追加
                viewModel.greetings.addAll(listOf(
                    Greeting("名前"),
                    Greeting("出身地"),
                    Greeting("職業/学業"),
                    Greeting("趣味"),
                    Greeting("好きな食べ物"),
                    Greeting("好きなスポーツ"),
                    Greeting("好きな国"),
                    Greeting("将来の目標"),
                    Greeting("興味のある分野"),
                    Greeting("今取り組んでいること"),
                    Greeting("好きな映画"),
                    Greeting("好きな音楽"),
                    Greeting("好きな場所"),
                    Greeting("得意なこと"),
                ))
            }
        }
    }




    // 終了時にデータを保存
    DisposableEffect(Unit) {
        onDispose {
            val jsonData = viewModel.serializeGreetings()
            Log.d("HomeScreen", "Saving greetings data: $jsonData")
            mainViewModel.setGreetingData(jsonData)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("detail")
                    Log.d("HomeScreen", "FloatingActionButton clicked")
                          },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        content = { padding ->
            Log.d("HomeScreen", "Scaffold content rendering")
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(bottom = 64.dp) // ボタンとの重なりを避けるために下に余白を追加
            ) {
                Text(
                    text = "自己紹介の項目",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                GreetingList(modifier = Modifier.fillMaxWidth(), viewModel = viewModel,mainViewModel = mainViewModel)
            }
        }
    )
}



@Composable
fun GreetingList(modifier: Modifier = Modifier, viewModel: GreetingViewModel, mainViewModel: MainViewModel) {
    val greetings = remember { viewModel.greetings }
    Log.d("GreetingList", "Number of greetings: ${greetings.size}") // リストのサイズを確認
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        items(viewModel.greetings) { greeting ->
            Log.d("GreetingList", "Rendering item: ${greeting.text}") // 各アイテムの描画確認
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${greeting.text} (${greeting.count})",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                )
                IconButton(
                    onClick = {
                        val index = viewModel.greetings.indexOf(greeting)
                        if (index != -1) {
                            greetings[index].count += 1
                            mainViewModel.setCount(index, greetings[index].count)
                            Log.d("GreetingList", "Updated count for ${greeting.text} to ${greetings[index].count}")
                        }
                    }
                ) {
                    Icon(Icons.Filled.ThumbUp, contentDescription = "Like")
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, viewModel: GreetingViewModel,mainViewModel: MainViewModel) {//新しい項目を追加する画面
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
                verticalArrangement = Arrangement.Center
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

                        if (newGreeting.isNotBlank()) { // newGreetingが空白でない場合のみ追加
                            viewModel.addGreeting(newGreeting, mainViewModel)
                            val jsonData = viewModel.serializeGreetings() // ここでデータをシリアライズ
                            mainViewModel.setGreetingData(jsonData) // DataStore に保存
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

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    // ダミーの DataStore を作成
    val dummyDataStore = AppDataStore(LocalContext.current)

    // MainViewModel を直接インスタンス化
    val dummyMainViewModel = MainViewModel(dummyDataStore)

    // プレビュー用の GreetingViewModel
    val dummyGreetingViewModel = GreetingViewModel()

    AndroidBootcampIwatePrefTheme {
        // MyApp に必要な引数を渡して呼び出す
        MyApp(
            navController = rememberNavController(),
            viewModel = dummyGreetingViewModel,
            mainViewModel = dummyMainViewModel
        )
    }
}


