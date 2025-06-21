package net.lailai.android.android_developers_japan_blog_reader.data

import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData

class BlogDataHolder() {
    var isDoneFirstLoaded: Boolean = false
    var data: BlogListData = BlogListData(emptyList())

    fun getBlogList(): Result<BlogListData> {
        if (!isDoneFirstLoaded) {
            return Result.failure(Exception("not loaded yet"))
        }
        isDoneFirstLoaded = true
        return Result.success(data)
    }
}
