package net.lailai.android.android_developers_japan_blog_reader.usecase

import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogMemoryRepository
import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogNetworkRepository
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

class GetBlogDataUseCase(
    private val blogMemoryRepository: BlogMemoryRepository,
    private val blogNetworkRepository: BlogNetworkRepository
) {
    suspend fun execute(isForce: Boolean): Result<BlogListData> {
        if (!isForce) {
            val memory = blogMemoryRepository.requestBlogList()
            if (memory.isSuccess) {
                return memory
            }
        }
        return blogNetworkRepository.requestRss().fold(
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
                return@fold Result.success(BlogListData(entryList))
            },
            onFailure = { e ->
                return@fold Result.failure(e)
            }
        )
    }
}
