package net.lailai.android.android_developers_japan_blog_reader.di

import net.lailai.android.android_developers_japan_blog_reader.data.BlogDataHolder
import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogMemoryRepository
import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogNetworkRepository
import net.lailai.android.android_developers_japan_blog_reader.usecase.GetBlogDataUseCase
import org.koin.dsl.module

val blogDataModule = module {
    single { GetBlogDataUseCase(get(), get()) }
    single { BlogMemoryRepository(get()) }
    single { BlogNetworkRepository(get()) }
    single { BlogDataHolder() }
}
