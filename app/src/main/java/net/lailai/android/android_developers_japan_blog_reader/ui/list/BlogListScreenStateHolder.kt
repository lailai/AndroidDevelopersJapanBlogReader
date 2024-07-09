package net.lailai.android.android_developers_japan_blog_reader.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Stable
class BlogListScreenStateHolder(
    private val _loadingState: StateFlow<LoadingState>
) {
    // 通信状態を見てプログレスを表示するとか考えてたけど面倒になってやめた
    val loadingState: LoadingState
        @Composable get() = _loadingState.collectAsStateWithLifecycle().value

    val shouldRefresh: Boolean
        get() = _loadingState.value is LoadingState.None
}
