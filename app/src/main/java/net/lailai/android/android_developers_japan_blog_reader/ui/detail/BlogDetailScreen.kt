package net.lailai.android.android_developers_japan_blog_reader.ui.detail

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import net.lailai.android.android_developers_japan_blog_reader.ui.theme.AndroidDevelopersJapanBlogReaderTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BlogDetailScreen(
    url: String,
    onBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Webサイトを表示するComposeがないのでAndroidViewを使ってWebViewを埋め込む
        AndroidView(
            modifier = Modifier.align(Alignment.Center),
            factory = { context ->
                WebView(context).apply {
                    if (url != "dummy") {
                        settings.apply {
                            javaScriptEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            setSupportZoom(true)
                        }
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                // ブラウザ開くのを抑止
                                return false
                            }
                        }
                        loadUrl(url)
                    }
                }
            }
        )
    }
    // Android 14の開発者オプションでオンにできる予測型「戻る」ジェスチャーを見てみたいがためにBackHandlerを入れてみる
    // 参考: https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture?hl=ja
    BackHandler {
        onBack()
    }
}

@Preview(
    device = Devices.PIXEL,
    showSystemUi = false,
    showBackground = true
)
@Composable
fun BlogDetailScreenPreview() {
    AndroidDevelopersJapanBlogReaderTheme {
        BlogDetailScreen(
            url = "dummy"
        )
    }
}
