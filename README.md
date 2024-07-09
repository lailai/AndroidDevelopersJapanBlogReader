# AndroidDevelopersJapanBlogReader
Jetpack Compose の勉強がてらに Android Developers Japan Blog を表示するアプリ

## やっていること
- [Android Developers Japan Blog](https://android-developers-jp.googleblog.com/) の [RSS](https://android-developers-jp.googleblog.com/atom.xml) を解析してブログ一覧を表示
- 一覧にある任意のブログをタップすると記事へ遷移
- 記事は WebView で表示 (Compose の AndroidView を利用)

## 既知の問題
- 記事を WebView で表示しているが WebView 内のリンクによる遷移を管理していないのでバックキーや戻るジェスチャーで一覧まで戻ってしまう
- Scaffold で TopAppBar や BottomAppBar を表示しているが特に意味はない
- 起動直後一覧表示や記事に遷移後の WebView 表示がアニメーションになっていないので美しくない
- 一覧で material3 の ListItem を利用しているが画像なしの記事は左マージンがそろっていなくて気になる (No Image 的なもの入れたほうが良さそうか)
- ViewModel のスコープがおそらくアプリケーションレベルになっているので Navigation Graph レベルくらいにしても良いかも

## Jetpack Compose を勉強した手順
- まずは Compose に触れてみよう
  - ということで何も考えずに UI を作っていく
  - [Compose レイアウトの基本](https://developer.android.com/develop/ui/compose/layouts/basics?hl=ja) を見ながらブログリスト作るのに良さそうなレイアウトを組んでいく
  - [この辺](https://developer.android.com/develop/ui/compose/layouts/basics?hl=ja#standard-layouts) がやりたいことまんまだった
  - その後 material3 に [ListItem](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ListItem(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function0,kotlin.Function0,kotlin.Function0,androidx.compose.material3.ListItemColors,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp)) なるものがあることを発見して採用 ([参照](https://developer.android.com/images/reference/androidx/compose/material3/lists.png))
- 再 Compose ってなんぞ？
  - Compose を使ってアプリを実装する上で避けては通れないのが再 Compose
  - そして今までの開発には無かった概念
  - [ここ](https://qiita.com/yasukotelin/items/fe1a36b3e8d9d21f9126) が勉強になった
  - Compose が持っている State や監視している変数の値が変わると呼び出されるようで、何度も繰り返し呼び出されても問題ないような設計にする必要あり
- 通信して RSS を解析し作った UI に流し込んでみる ついでに PullToRefresh も入れてみる
  - ViewModel やら Repository やら作って Retrofit と OkHttp で通信する
  - XML 解析は TikXml を使って Retrofit のコンバータに突っ込む
  - ブログ一覧を表示するために必要な XML のタグを探すのが手間だった
  - PullToRefresh は調べてみると 1.3.0-beta のやり方ばっかり載っていて 1.2.1 のやり方はあまり参考になりそうなのがなく自力
- [状態ホイスティング](https://developer.android.com/develop/ui/compose/state?hl=ja#state-hoisting) という考え方があるらしい
  - ステートフルな Composable をステートレスな Composable にして外部からの入力を受け付けられるようにする手法っぽい
- [状態ホルダー](https://developer.android.com/develop/ui/compose/state?hl=ja#managing-state) という考え方もある
  - 監視すべきパラメータが多くなってきたり UI ロジックが増えてきたらそれらを一括でまとめて分離する手法っぽい
- そこまでやったら今度は画面遷移でブログ一覧から記事に飛べるようにする
  - Navigation の出番
  - 後で紹介しているライブラリの Navigation の参考サイトを見ながらセットアップしただけ
- [Scaffold](https://developer.android.com/develop/ui/compose/components/scaffold?hl=ja) について触れていないことを思い出してアプリの体裁を整えるために TopAppBar と BottomAppBar を設定して力尽きる

## 使用するライブラリ
Android Studio の新規プロジェクトで自動で入ったもの以外のライブラリを記載

### プラグイン
- org.jetbrains.kotlin.kapt
  - [kapt (Kotlin Annotation Processing Tool)](https://kotlinlang.org/docs/kapt.html) はアノテーションを利用して自動コードが生成できる
  - Java でいうところの Annotation Processor と同じ
  - 本プロジェクトでは RSS 解析で XML の読み取りをしており、 data class へのコンバーターを利用するために設定
  - もっと高速の [ksp (Kotlin Symbol Processing)](https://kotlinlang.org/docs/ksp-overview.html) があるが本プロジェクトで利用したいライブラリでは未対応だったので kapt を採用
- org.jetbrains.kotlin.plugin.serialization
  - テキストからオブジェクトへの変換、またはその逆をしてくれる
  - 本プロジェクトでは Navigation で画面遷移で引数を渡すために利用している
    - が、ぶっちゃけ [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html) を使うまでもなくテキストで良かった

### ライブラリ
- androidx.lifecycle:lifecycle-runtime-compose
  - Compose 関連の便利拡張関数セット
  - [Flow.collectAsStateWithLifecycle()](https://developer.android.com/develop/ui/compose/state?hl=ja#use-other-types-of-state-in-jetpack-compose) のために使用
- androidx.navigation:navigation-compose
  - Compose 用 Navigation を利用
  - [Navigation](https://developer.android.com/develop/ui/compose/navigation?hl=ja) は画面遷移関連を司るライブラリ
- io.coil-kt:coil-compose
  - [画像読み込みライブラリ](https://coil-kt.github.io/coil/README-ja/) で Compose を利用したアプリではよく使われる
  - [Picasso](https://square.github.io/picasso/) や [Glide](https://bumptech.github.io/glide/) と同じようなもの
- io.insert-koin:koin-androidx-compose
  - [Kotlin に特化した DI (Dependency Injection: 依存性注入) ライブラリ](https://insert-koin.io/)
  - [Dagger Hilt](https://dagger.dev/hilt/) などと同じ
  - [DI](https://developer.android.com/training/dependency-injection?hl=ja) については Dagger Hilt の説明ではあるが Android Developers にも [ガイド](https://developer.android.com/training/dependency-injection/hilt-android?hl=ja) がある
  - なお、本プロジェクトではあまり強みを活かせていない
- org.jetbrains.kotlinx:kotlinx-serialization-json
  - プラグインの org.jetbrains.kotlin.plugin.serialization を参照
  - json となっているが JSON を利用している訳ではなく、ライブラリに含まれる機能を利用しているだけ
- com.squareup.okhttp3:okhttp
- com.squareup.okhttp3:logging-interceptor
  - 言わずとしれた [HTTP クライアント](https://square.github.io/okhttp/) とログ出力用の Interceptor
- com.squareup.retrofit2:retrofit
  - 言わずとしれた [HTTP API を利用するためのライブラリ](https://square.github.io/retrofit/)
- com.tickaroo.tikxml:core
- com.tickaroo.tikxml:annotation
- com.tickaroo.tikxml:retrofit-converter
- com.tickaroo.tikxml:processor
  - [XML 解析用ライブラリ](https://github.com/Tickaroo/tikxml)
  - 本当は retrofit 謹製のコンバータ (com.squareup.retrofit2:converter-simplexml) を利用したかったが非推奨になってしまっていた
  - ドキュメントには別のコンバータ (com.squareup.retrofit2:converter-jaxb) を利用するように書かれていた
  - そちらのドキュメントを見ると Android には非対応とのこと
  - retrofit のコンバータとして設定できる tikxml を採用 (バージョン 0.8.15 は罠)
