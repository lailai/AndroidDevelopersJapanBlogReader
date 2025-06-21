package net.lailai.android.android_developers_japan_blog_reader.ui.list

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import net.lailai.android.android_developers_japan_blog_reader.usecase.GetBlogDataUseCase
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

@Stable
class BlogListScreenStateHolder(
    private val useCase: GetBlogDataUseCase
) {
    var isRefreshing: Boolean by mutableStateOf(false)
        private set

    var isSuccess: Boolean by mutableStateOf(true)
        private set

    var errorMessage: String? by mutableStateOf(null)
        private set

    var data: BlogListData by mutableStateOf(BlogListData(emptyList()))
        private set

    suspend fun onRefresh(isForce: Boolean = false) {
        isRefreshing = true
        useCase.execute(isForce).collect { result ->
            result.fold(
                onSuccess = { blogData ->
                    isSuccess = true
                    data = blogData
                },
                onFailure = { e ->
                    isSuccess = false
                    errorMessage = e.localizedMessage
                }
            )
        }
        isRefreshing = false
    }
}
