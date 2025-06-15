package net.lailai.android.android_developers_japan_blog_reader.ui.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    onNavigateToBlogDetail: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: MainViewModel = koinViewModel(),
    // 状態ホルダーとやらを採用してみる
    state: BlogListScreenStateHolder = rememberBlogListScreenState(
        loadingState = viewModel.loadingState.collectAsStateWithLifecycle(),
        data = viewModel.data.collectAsStateWithLifecycle()
    )
) {
    LaunchedEffect(Unit) {
        viewModel.getBlogList()
    }

    val scope = rememberCoroutineScope()
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            scope.launch {
                viewModel.getBlogList()
            }
        },
        modifier = Modifier
    ) {
        BlogListScreen(
            state.data,
            onNavigateToBlogDetail
        )
    }

    if (!state.isSuccess && state.errorMessage != null) {
        scope.launch {
            // エラー表示
            snackbarHostState.showSnackbar(state.errorMessage.orEmpty())
        }
    }
}

// ViewModelを引数に入れるとPreviewが動作しなくなるので分離
@Composable
fun BlogListScreen(
    data: State<BlogListData>,
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
    }
}

@Composable
private fun rememberBlogListScreenState(
    loadingState: State<LoadingState>,
    data: State<BlogListData>
): BlogListScreenStateHolder = remember(loadingState) {
    BlogListScreenStateHolder(loadingState, data)
}

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
