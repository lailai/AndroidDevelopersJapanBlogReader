package net.lailai.android.android_developers_japan_blog_reader.ui.list

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

@Stable
class BlogListScreenStateHolder(
    private val _loadingState: State<LoadingState>,
    val data: State<BlogListData>
) {
    val isRefreshing: Boolean
        get() = _loadingState.value is LoadingState.Processing

    val isSuccess: Boolean
        get() = _loadingState.value is LoadingState.Success

    val errorMessage: String?
        get() = (_loadingState.value as? LoadingState.Error)?.message
}
