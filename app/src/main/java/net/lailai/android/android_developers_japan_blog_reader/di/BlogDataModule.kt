package net.lailai.android.android_developers_japan_blog_reader.di

import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogRepository
import net.lailai.android.android_developers_japan_blog_reader.ui.list.BlogDataHolder
import org.koin.dsl.module

val blogDataModule = module {
    single { BlogDataHolder(get()) }
    single { BlogRepository(get()) }
}
