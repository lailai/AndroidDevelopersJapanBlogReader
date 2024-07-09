package net.lailai.android.android_developers_japan_blog_reader.data.repository

import android.util.Log
import net.lailai.android.android_developers_japan_blog_reader.data.BlogService
import net.lailai.android.android_developers_japan_blog_reader.data.entity.rss.Feed

class BlogRepository(private val service: BlogService) {
    suspend fun requestRss(): Result<Feed> {
        Log.d(TAG, "[requestRss]")
        val rssResponse = service.getRss()
        if (rssResponse.isSuccessful) {
            val feed = rssResponse.body()!!
            Log.d(TAG, "[requestRss] feed=$feed")
            return Result.success(feed)
        } else {
            val errorMessage = rssResponse.errorBody()?.string()
            Log.e(TAG, "[requestRss] errorMessage=$errorMessage")
            return Result.failure(Exception(errorMessage))
        }
    }

    companion object {
        private val TAG = BlogRepository::class.simpleName.orEmpty()
    }
}
