package net.lailai.android.android_developers_japan_blog_reader.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogDatabaseRepository
import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogMemoryRepository
import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogNetworkRepository
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

class GetBlogDataUseCase(
    private val blogMemoryRepository: BlogMemoryRepository,
    private val blogDatabaseRepository: BlogDatabaseRepository,
    private val blogNetworkRepository: BlogNetworkRepository
) {
    fun execute(isForce: Boolean): Flow<Result<BlogListData>> = flow {
        if (!isForce) {
            val memory = blogMemoryRepository.requestBlogList()
            if (memory.isSuccess) {
                emit(Result.success(memory.getOrNull() ?: BlogListData(emptyList())))
                return@flow
            } else {
                val database = blogDatabaseRepository.requestBlogList()
                if (database.isSuccess) {
                    database.getOrNull()?.let {
                        blogMemoryRepository.updateBlogList(it)
                    }
                    emit(Result.success(database.getOrNull() ?: BlogListData(emptyList())))
                }
            }
        }
        blogNetworkRepository.requestRss().fold(
            onSuccess = { feed ->
                val entryList = feed.entries.map { entry ->
                    BlogListData.Entry(
                        entry.id,
                        entry.title,
                        entry.updated,
                        entry.media?.url,
                        entry.links.first { it.rel == "alternate" }.href
                    )
                }
                blogMemoryRepository.updateBlogList(BlogListData(entryList))
                blogDatabaseRepository.updateBlogList(BlogListData(entryList))
                emit(Result.success(BlogListData(entryList)))
            },
            onFailure = { e ->
                emit(Result.failure(e))
            }
        )
    }
}
