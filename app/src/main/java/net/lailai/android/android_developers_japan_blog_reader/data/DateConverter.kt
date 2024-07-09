package net.lailai.android.android_developers_japan_blog_reader.data

import com.tickaroo.tikxml.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateConverter : TypeConverter<Date> {
    private val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.JAPAN)

    override fun read(value: String?): Date {
        return dateFormat.parse(value!!)!!
    }

    override fun write(value: Date?): String {
        return dateFormat.format(value!!)
    }

    companion object {
        private const val DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    }
}
