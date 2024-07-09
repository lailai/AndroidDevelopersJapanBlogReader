package net.lailai.android.android_developers_japan_blog_reader.data

import net.lailai.android.android_developers_japan_blog_reader.data.entity.rss.Feed
import retrofit2.Response
import retrofit2.http.GET

interface BlogService {
    @GET("/atom.xml")
    suspend fun getRss(): Response<Feed>
}
