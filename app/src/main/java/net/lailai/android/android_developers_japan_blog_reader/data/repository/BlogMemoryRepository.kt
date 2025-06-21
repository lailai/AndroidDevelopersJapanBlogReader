package net.lailai.android.android_developers_japan_blog_reader.data.repository

import android.util.Log
import net.lailai.android.android_developers_japan_blog_reader.data.BlogDataHolder
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

class BlogMemoryRepository(private val dataHolder: BlogDataHolder) {
    fun requestBlogList(): Result<BlogListData> {
        Log.d(TAG, "[requestBlogList]")
        return dataHolder.getBlogList()
    }

    fun updateBlogList(data: BlogListData) {
        Log.d(TAG, "[updateBlogList] data=$data")
        dataHolder.isDoneFirstLoaded = true
        dataHolder.data = data
    }

    companion object {
        private val TAG = BlogMemoryRepository::class.simpleName.orEmpty()
    }
}
