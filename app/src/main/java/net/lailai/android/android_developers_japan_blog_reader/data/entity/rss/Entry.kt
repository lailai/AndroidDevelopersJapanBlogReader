package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.util.Date

data class Entry(
    @JacksonXmlProperty(localName = "id")
    val id: String,
    @JacksonXmlProperty(localName = "updated")
    val updated: Date,
    @JacksonXmlProperty(localName = "title")
    val title: String,
    @JacksonXmlElementWrapper(localName = "link", useWrapping = false)
    @JacksonXmlProperty(localName = "link")
    val links: List<Link>,
    @JacksonXmlProperty(namespace = "http://search.yahoo.com/mrss/", localName = "thumbnail")
    val media: Media?
)
