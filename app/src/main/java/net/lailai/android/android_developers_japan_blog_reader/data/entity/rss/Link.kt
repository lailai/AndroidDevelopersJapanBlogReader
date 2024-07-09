package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Link(
    @Attribute(name = "href") val href: String,
    @Attribute(name = "rel") val rel: String,
    @Attribute(name = "title") val title: String?,
    @Attribute(name = "type") val type: String
)
