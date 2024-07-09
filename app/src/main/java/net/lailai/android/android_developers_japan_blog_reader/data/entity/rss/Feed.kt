package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Feed(
    @PropertyElement(name = "title")
    val title: String,
    @Element(name = "entry")
    val entries: List<Entry>
)
