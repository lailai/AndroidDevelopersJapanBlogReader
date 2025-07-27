package net.lailai.android.android_developers_japan_blog_reader.data.entity.rss

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Media(
    @param:JacksonXmlProperty(localName = "url")
    val url: String
)
