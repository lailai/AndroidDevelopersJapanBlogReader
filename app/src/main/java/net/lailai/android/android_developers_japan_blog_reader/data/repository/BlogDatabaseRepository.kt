package net.lailai.android.android_developers_japan_blog_reader.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.lailai.android.android_developers_japan_blog_reader.data.entity.database.BlogEntry
import net.lailai.android.android_developers_japan_blog_reader.di.BlogEntryDao
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

class BlogDatabaseRepository(
    private val blogDao: BlogEntryDao
) {
    suspend fun requestBlogList(): Result<BlogListData> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "[requestBlogList]")
            val blogList = blogDao.getAll()
            if (blogList.isEmpty()) {
                throw Exception("No data")
            }
            Result.success(
                BlogListData(
                    blogList.map { entry ->
                        BlogListData.Entry(
                            entry.id,
                            entry.title,
                            entry.date,
                            entry.imageUrl,
                            entry.link
                        )
                    }
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBlogList(data: BlogListData) = withContext(Dispatchers.IO) {
        blogDao.insertAll(
            data.blogList.map { entry ->
                BlogEntry(
                    entry.id,
                    entry.title,
                    entry.date,
                    entry.imageUrl,
                    entry.link
                )
            }
        )
    }

    companion object {
        private val TAG = BlogDatabaseRepository::class.simpleName.orEmpty()
    }
}
