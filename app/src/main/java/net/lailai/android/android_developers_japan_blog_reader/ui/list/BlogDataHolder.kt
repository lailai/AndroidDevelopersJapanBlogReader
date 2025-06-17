package net.lailai.android.android_developers_japan_blog_reader.ui.list

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogRepository
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

class BlogDataHolder(
    private val repository: BlogRepository
) {
    val isDoneFirstLoaded: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val loadingState: StateFlow<LoadingState>
        field = MutableStateFlow<LoadingState>(LoadingState.None)

    val data: StateFlow<BlogListData>
        field = MutableStateFlow<BlogListData>(BlogListData(listOf()))

    suspend fun getBlogList() {
        isDoneFirstLoaded.value = true
        loadingState.value = LoadingState.Processing
        // 機内モードなどで通信処理が走る前に失敗すると状態が反映されないまま次の状態に遷移したりするのでわずかに間を入れる
        delay(100L)
        repository.requestRss().fold(
            onSuccess = { feed ->
                val entryList = feed.entries.map { entry ->
                    BlogListData.Entry(
                        entry.title,
                        entry.updated,
                        entry.media?.url,
                        entry.links.first { it.rel == "alternate" }.href
                    )
                }
                data.value = BlogListData(entryList)
                loadingState.value = LoadingState.Success
            },
            onFailure = {
                loadingState.value = LoadingState.Error(it.message.orEmpty())
            }
        )
    }
}
