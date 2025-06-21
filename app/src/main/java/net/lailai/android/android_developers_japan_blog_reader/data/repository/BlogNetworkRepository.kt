package net.lailai.android.android_developers_japan_blog_reader.data.repository

import android.util.Log
import net.lailai.android.android_developers_japan_blog_reader.data.BlogService
import net.lailai.android.android_developers_japan_blog_reader.data.entity.rss.Feed

class BlogNetworkRepository(private val service: BlogService) {
    suspend fun requestRss(): Result<Feed> {
        Log.d(TAG, "[requestRss]")
        return try {
            val rssResponse = service.getRss()
            if (rssResponse.isSuccessful) {
                val feed = rssResponse.body()!!
                Result.success(feed)
            } else {
                val errorMessage = rssResponse.errorBody()?.string()
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private val TAG = BlogNetworkRepository::class.simpleName.orEmpty()
    }
}
