package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Link(
    @JacksonXmlProperty(localName = "href")
    val href: String,
    @JacksonXmlProperty(localName = "rel")
    val rel: String,
    @JacksonXmlProperty(localName = "title")
    val title: String?,
    @JacksonXmlProperty(localName = "type")
    val type: String
)
