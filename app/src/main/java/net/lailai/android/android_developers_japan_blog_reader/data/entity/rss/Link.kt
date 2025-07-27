package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Link(
    @param:JacksonXmlProperty(localName = "href")
    val href: String,
    @param:JacksonXmlProperty(localName = "rel")
    val rel: String,
    @param:JacksonXmlProperty(localName = "title")
    val title: String?,
    @param:JacksonXmlProperty(localName = "type")
    val type: String
)
