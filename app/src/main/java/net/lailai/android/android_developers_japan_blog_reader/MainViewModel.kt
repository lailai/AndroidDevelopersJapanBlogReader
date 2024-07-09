package net.lailai.android.android_developers_japan_blog_reader

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogRepository
import net.lailai.android.android_developers_japan_blog_reader.ui.list.LoadingState
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

class MainViewModel(
    private val repository: BlogRepository
) : ViewModel() {
    private val _data = MutableStateFlow(BlogListData(listOf()))
    val data = _data.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.None)
    val loadingState = _loadingState.asStateFlow()

    suspend fun getBlogList() {
        _loadingState.value = LoadingState.Processing
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
                _data.value = BlogListData(entryList)
                _loadingState.value = LoadingState.Success
            },
            onFailure = {
                _loadingState.value = LoadingState.Error(it.message.orEmpty())
            }
        )
    }
}
