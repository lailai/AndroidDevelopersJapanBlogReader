package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "feed")
data class Feed(
    @param:JacksonXmlProperty(localName = "title")
    val title: String,
    @param:JacksonXmlElementWrapper(localName = "entry", useWrapping = false)
    @param:JacksonXmlProperty(localName = "entry")
    val entries: List<Entry>
)
