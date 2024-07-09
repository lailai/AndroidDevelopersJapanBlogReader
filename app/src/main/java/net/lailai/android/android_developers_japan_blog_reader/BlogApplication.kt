package net.lailai.android.android_developers_japan_blog_reader

import android.app.Application
import net.lailai.android.android_developers_japan_blog_reader.di.appModule
import net.lailai.android.android_developers_japan_blog_reader.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BlogApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BlogApplication)
            modules(appModule, networkModule)
        }
    }
}
