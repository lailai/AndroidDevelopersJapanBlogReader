package net.lailai.android.android_developers_japan_blog_reader.ui.list

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import net.lailai.android.android_developers_japan_blog_reader.data.MessageUtil
import net.lailai.android.android_developers_japan_blog_reader.usecase.GetBlogDataUseCase
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

@Stable
class BlogListScreenStateHolder(
    private val context: Context,
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
        Log.d(TAG, "[onRefresh] isForce=$isForce")
        isRefreshing = true
        useCase.execute(isForce).collect { result ->
            result.fold(
                onSuccess = { blogData ->
                    Log.d(TAG, "[onRefresh] success")
                    Log.d(TAG, "[onRefresh] blogData=$blogData")
                    isSuccess = true
                    data = blogData
                },
                onFailure = { e ->
                    Log.w(TAG, "[onRefresh] failure")
                    Log.w(TAG, "[onRefresh] e.localizedMessage=${e.localizedMessage}")
                    // UI反映が間に合わないほど早く例外が投げられる場合があるのでディレイを入れる
                    delay(100L)
                    isSuccess = false
                    errorMessage = MessageUtil.mapToErrorMessage(context, e)
                }
            )
        }
        isRefreshing = false
    }

    /**
     * エラーメッセージ表示後に状態を初期化する
     */
    fun errorMessageShown() {
        Log.d(TAG, "[errorMessageShown]")
        isSuccess = false
        errorMessage = null
    }

    companion object {
        private val TAG = BlogListScreenStateHolder::class.simpleName.orEmpty()
    }
}
