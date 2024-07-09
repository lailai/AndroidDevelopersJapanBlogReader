package net.lailai.android.android_developers_japan_blog_reader.usecase.param

import java.util.Date

data class BlogListData(
    val blogList: List<Entry>
) {
    data class Entry(
        val title: String,
        val date: Date,
        val imageUrl: String?,
        val link: String
    )
}
