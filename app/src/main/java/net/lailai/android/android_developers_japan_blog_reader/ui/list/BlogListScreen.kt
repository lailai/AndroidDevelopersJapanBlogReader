package net.lailai.android.android_developers_japan_blog_reader.ui.list

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
fun BlogListContent(
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
        BlogListContent(
            state.data,
            onNavigateToBlogDetail
        )
    }

    // エラーハンドリング
    val shouldShowError: Boolean by remember {
        derivedStateOf { !state.isSuccess && state.errorMessage != null }
    }
    LaunchedEffect(shouldShowError) {
        if (shouldShowError) {
            // エラー表示
            snackbarHostState.showSnackbar(state.errorMessage.orEmpty())
            state.errorMessageShown()
        }
    }
}

@Composable
fun BlogListContent(
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
    context: Context = LocalContext.current.applicationContext,
    useCase: GetBlogDataUseCase = koinInject()
): BlogListScreenStateHolder = remember {
    BlogListScreenStateHolder(context, useCase)
}

@Preview(
    device = Devices.PIXEL,
    showSystemUi = false,
    showBackground = true
)
@Composable
fun BlogListPreview() {
    AndroidDevelopersJapanBlogReaderTheme {
        BlogListContent(
            BlogListData(
                List(10) {
                    BlogListData.Entry("id", "テスト", Date(), "dummy", "dummy")
                }
            )
        )
    }
}
