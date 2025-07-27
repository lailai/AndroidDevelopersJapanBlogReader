package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.util.Date

data class Entry(
    @param:JacksonXmlProperty(localName = "id")
    val id: String,
    @param:JacksonXmlProperty(localName = "updated")
    val updated: Date,
    @param:JacksonXmlProperty(localName = "title")
    val title: String,
    @param:JacksonXmlElementWrapper(localName = "link", useWrapping = false)
    @param:JacksonXmlProperty(localName = "link")
    val links: List<Link>,
    @param:JacksonXmlProperty(namespace = "http://search.yahoo.com/mrss/", localName = "thumbnail")
    val media: Media?
)
