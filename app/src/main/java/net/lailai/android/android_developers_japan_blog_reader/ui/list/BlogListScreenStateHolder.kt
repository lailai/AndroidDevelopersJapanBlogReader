package net.lailai.android.android_developers_japan_blog_reader.ui.list

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

@Stable
class BlogListScreenStateHolder(
    private val isDoneFirstLoadedState: State<Boolean>,
    private val loadingState: State<LoadingState>,
    val data: State<BlogListData>
) {
    val isDoneFirstLoaded: Boolean
        get() = isDoneFirstLoadedState.value

    val isRefreshing: Boolean
        get() = loadingState.value is LoadingState.Processing

    val isSuccess: Boolean
        get() = loadingState.value is LoadingState.Success

    val errorMessage: String?
        get() = (loadingState.value as? LoadingState.Error)?.message
}
