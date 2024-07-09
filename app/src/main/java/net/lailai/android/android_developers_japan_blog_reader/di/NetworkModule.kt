package net.lailai.android.android_developers_japan_blog_reader.di

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import net.lailai.android.android_developers_japan_blog_reader.data.BlogService
import net.lailai.android.android_developers_japan_blog_reader.data.DateConverter
import net.lailai.android.android_developers_japan_blog_reader.di.NetworkModule.provideConverterFactory
import net.lailai.android.android_developers_japan_blog_reader.di.NetworkModule.provideOkHttpClient
import net.lailai.android.android_developers_japan_blog_reader.di.NetworkModule.provideRetrofit
import net.lailai.android.android_developers_japan_blog_reader.di.NetworkModule.provideService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create
import java.util.Date

// DI (Koin) の設定(ネットワーク系)
val networkModule = module {
    single { provideOkHttpClient() }
    single { provideConverterFactory() }
    single { provideRetrofit(get(), get()) }
    single { provideService(get()) }
}

object NetworkModule {
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    fun provideConverterFactory(): TikXmlConverterFactory = TikXmlConverterFactory.create(
        TikXml.Builder()
            .exceptionOnUnreadXml(false)
            .addTypeConverter(Date::class.java, DateConverter())
            .build()
    )

    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        tikXmlConverterFactory: TikXmlConverterFactory
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://android-developers-jp.googleblog.com/")
        .addConverterFactory(tikXmlConverterFactory)
        .build()

    fun provideService(retrofit: Retrofit): BlogService = retrofit.create()
}
