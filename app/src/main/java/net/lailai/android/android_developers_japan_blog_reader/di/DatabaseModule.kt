package net.lailai.android.android_developers_japan_blog_reader.di

import android.app.Application
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import net.lailai.android.android_developers_japan_blog_reader.data.entity.database.BlogEntry
import net.lailai.android.android_developers_japan_blog_reader.di.DatabaseModule.provideBlogEntryDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val databaseModule = module {
    single { provideBlogEntryDatabase(androidApplication()) }
    single { get<BlogReaderDatabase>().blogEntryDao() }
}

object DatabaseModule {
    fun provideBlogEntryDatabase(application: Application): BlogReaderDatabase =
        Room.databaseBuilder(
            application,
            BlogReaderDatabase::class.java,
            "blog_reader_database"
        ).build()
}

@Database(entities = [BlogEntry::class], version = 1)
@TypeConverters(BlogReaderDatabaseConverter::class)
abstract class BlogReaderDatabase : RoomDatabase() {
    abstract fun blogEntryDao(): BlogEntryDao
}

class BlogReaderDatabaseConverter {
    @TypeConverter
    fun fromDate(date: Date?): String? {
        date ?: return null
        val dateFormat = SimpleDateFormat(DateConverterFactory.DATE_FORMAT_PATTERN, Locale.JAPAN)
        return dateFormat.format(date)
    }

    @TypeConverter
    fun toDate(dateString: String?): Date? {
        dateString ?: return null
        val dateFormat = SimpleDateFormat(DateConverterFactory.DATE_FORMAT_PATTERN, Locale.JAPAN)
        return dateFormat.parse(dateString)
    }
}

@Dao
interface BlogEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(blogEntries: List<BlogEntry>)

    @Query("SELECT * FROM blog_entries ORDER BY date DESC")
    fun getAll(): List<BlogEntry>
}
