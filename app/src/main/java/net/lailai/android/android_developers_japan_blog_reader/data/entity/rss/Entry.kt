package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import java.util.Date

@Xml
data class Entry(
    @PropertyElement(name = "id") val id: String,
    @PropertyElement(name = "updated") val updated: Date,
    @PropertyElement(name = "title") val title: String,
    @Element(name = "link") val links: List<Link>,
    @Element(name = "media:thumbnail") val media: Media?
)
