package net.lailai.android.android_developers_japan_blog_reader.ui.list

import android.annotation.SuppressLint
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import net.lailai.android.android_developers_japan_blog_reader.ui.theme.AndroidDevelopersJapanBlogReaderTheme
import net.lailai.android.android_developers_japan_blog_reader.usecase.GetBlogDataUseCase
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData
import org.koin.compose.koinInject
import java.util.Date

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogListScreen(
    onNavigateToBlogDetail: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    state: BlogListScreenStateHolder = rememberBlogListScreenState()
) {
    LaunchedEffect(Unit) {
        state.onRefresh()
    }

    val scope = rememberCoroutineScope()
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            scope.launch {
                state.onRefresh(true)
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
    data: BlogListData,
    onNavigateToBlogDetail: (String) -> Unit = {}
) {
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(data.blogList) { entry ->
                BlogListItem(entry, onNavigateToBlogDetail)
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun rememberBlogListScreenState(
    useCase: GetBlogDataUseCase = koinInject()
): BlogListScreenStateHolder = remember {
    BlogListScreenStateHolder(useCase)
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
            BlogListData(
                List(10) {
                    BlogListData.Entry("id", "テスト", Date(), "dummy", "dummy")
                }
            )
        )
    }
}
