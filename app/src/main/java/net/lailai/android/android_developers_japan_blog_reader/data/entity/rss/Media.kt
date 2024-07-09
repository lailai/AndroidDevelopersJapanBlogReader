package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Media(
    @Attribute(name = "url") val url: String
)
