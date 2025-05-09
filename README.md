# 自己紹介の項目を共有するアプリケーション

## 作成した目的・背景
- サイバーエージェント様のAndroidブートキャンプへの参加時に作成したサンプルアプリです。
- 自己紹介を書く際、どの項目に何を書けばよいか悩む点から、他の人の入力例を共有できればと考え作成しました。

## ターゲット
- 自己紹介を書く際、何を書くか悩んでいる人

## プロジェクトの概要
- ホーム画面に多くのユーザーが記入した項目を表示する
- 画面右下の追加ボタンを押すと、項目を新規に追加できる
- 各項目に対して「いいね」機能が実装されている

## 今後の展望
- 項目のソート機能の実装
- 各項目に対するコメント機能の実装

## 特徴
- **Jetpack Compose** を使用したモダンなUI開発  
- **Android DataStore** による非同期なデータ保存  
- **ViewModel** による状態管理  
- シンプルな画面遷移（HomeScreen と DetailScreen）

## ディレクトリ構成
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/androidbootcampiwatepref/
│   │   │   ├── MainActivity.kt         // アプリのエントリーポイント
│   │   │   ├── data/
│   │   │   │   └── store/
│   │   │   │       └── AppDataStore.kt   // DataStore 実装
│   │   │   ├── ui/
│   │   │   │   ├── screen/
│   │   │   │   │   ├── HomeScreen.kt     // Greeting 画面（HomeScreen と DetailScreen）
│   │   │   │   │   └── DetailScreen.kt
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt          // カラーテーマ
│   │   │   │       ├── Theme.kt          // テーマ設定
│   │   │   │       └── Type.kt           // テキストスタイル
│   │   │   └── viewmodel/
│   │   │       ├── MainViewModel.kt      // DataStore 連携用 ViewModel
│   │   │       └── GreetingViewModel.kt  // Greeting 機能の状態管理
│   │   └── res/                          // 各種リソース（文字列、アイコンなど）
├── build.gradle                          // プロジェクト全体の Gradle 設定
├── settings.gradle                       // Gradle セットアップ
└── [README.md](http://_vscodecontentref_/0)    
```

## 技術スタック
- Kotlin
- Jetpack Compose
- Android DataStore
- AndroidX Lifecycle (ViewModel, LiveData)

## 環境要件
- Android Studio Arctic Fox 以降（または IntelliJ IDEA）
- Android SDK 34
- Minimum SDK: 24

## ビルドおよび実行方法
ビルド
ターミナル上で以下の Gradle コマンドを実行してデバッグビルドを作成します。
```
./gradlew assembleDebug
```

インストール
エミュレータまたは実機へインストールするには、以下のコマンドを実行してください。
```
./gradlew installDebug
```

## 自分用メモ
- ビルド、インストール、実行に関する情報は、作業効率向上のためにこちらに記載しています。
```
エミュレータの起動
/Users/tsuchida/Library/Android/sdk/emulator/emulator -avd Pixel_7_API_32

アプリの起動
adb shell am start -n com.example.androidbootcampiwatepref/.MainActivity
```