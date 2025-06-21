package net.lailai.android.android_developers_japan_blog_reader.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import net.lailai.android.android_developers_japan_blog_reader.BuildConfig
import net.lailai.android.android_developers_japan_blog_reader.data.BlogService
import net.lailai.android.android_developers_japan_blog_reader.di.NetworkModule.provideDateConverter
import net.lailai.android.android_developers_japan_blog_reader.di.NetworkModule.provideOkHttpClient
import net.lailai.android.android_developers_japan_blog_reader.di.NetworkModule.provideRetrofit
import net.lailai.android.android_developers_japan_blog_reader.di.NetworkModule.provideService
import net.lailai.android.android_developers_japan_blog_reader.di.NetworkModule.provideXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// DI (Koin) の設定(ネットワーク系)
val networkModule = module {
    single { provideOkHttpClient() }
    single { provideXmlConverterFactory() }
    single { provideDateConverter() }
    single { provideRetrofit(get(), get(), get()) }
    single { provideService(get()) }
}

object NetworkModule {
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
        )
        .build()

    fun provideXmlConverterFactory(): XmlConverterFactory = XmlConverterFactory()

    fun provideDateConverter(): DateConverterFactory = DateConverterFactory()

    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        xmlConverterFactory: XmlConverterFactory,
        dateConverterFactory: DateConverterFactory
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://android-developers-jp.googleblog.com/")
        .addConverterFactory(xmlConverterFactory)
        .addConverterFactory(dateConverterFactory)
        .build()

    fun provideService(retrofit: Retrofit): BlogService = retrofit.create()
}

class XmlConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val xmlMapper = XmlMapper().apply {
            registerKotlinModule()
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
        val javaType = xmlMapper.typeFactory.constructType(type)
        return Converter<ResponseBody, Any> { responseBody ->
            responseBody.use {
                xmlMapper.readValue(it.charStream(), javaType)
            }
        }
    }
}

class DateConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Date> {
        return Converter<ResponseBody, Date> { responseBody ->
            responseBody.use {
                val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.JAPAN)
                dateFormat.parse(it.string())
            }
        }
    }

    companion object {
        const val DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    }
}
