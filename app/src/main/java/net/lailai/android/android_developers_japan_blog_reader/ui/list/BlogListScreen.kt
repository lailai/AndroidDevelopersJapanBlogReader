package net.lailai.android.android_developers_japan_blog_reader.ui.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.lailai.android.android_developers_japan_blog_reader.MainViewModel
import net.lailai.android.android_developers_japan_blog_reader.ui.theme.AndroidDevelopersJapanBlogReaderTheme
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogListScreen(
    pullToRefreshState: PullToRefreshState,
    onNavigateToBlogDetail: (String) -> Unit,
    viewModel: MainViewModel = koinViewModel(),
    // 状態ホルダーとやらを採用してみる
    state: BlogListScreenStateHolder = rememberBlogListScreenState(
        loadingState = viewModel.loadingState
    )
) {
    val scope = rememberCoroutineScope()
    // if文の条件式の順番を変えるだけで一覧更新が動かなくなるのが全く分からなかった
    if (pullToRefreshState.isRefreshing || state.shouldRefresh) {
        scope.launch {
            viewModel.getBlogList()
            pullToRefreshState.endRefresh()
        }
    }
    BlogListScreen(
        viewModel.data.collectAsStateWithLifecycle(),
        pullToRefreshState,
        onNavigateToBlogDetail
    )
}

// ViewModelを引数に入れるとPreviewが動作しなくなるので分離
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BlogListScreen(
    data: State<BlogListData>,
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    onNavigateToBlogDetail: (String) -> Unit = {}
) {
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(data.value.blogList) { entry ->
                BlogListItem(entry, onNavigateToBlogDetail)
                HorizontalDivider()
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState
        )
    }
}

@Composable
private fun rememberBlogListScreenState(
    loadingState: StateFlow<LoadingState>
): BlogListScreenStateHolder = remember(loadingState) {
    BlogListScreenStateHolder(loadingState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    device = Devices.PIXEL,
    showSystemUi = false,
    showBackground = true
)
@Composable
fun BlogListPreview() {
    AndroidDevelopersJapanBlogReaderTheme {
        BlogListScreen(
            remember {
                mutableStateOf(
                    BlogListData(
                        List(10) {
                            BlogListData.Entry("テスト", Date(), "dummy", "dummy")
                        }
                    )
                )
            }
        )
    }
}
