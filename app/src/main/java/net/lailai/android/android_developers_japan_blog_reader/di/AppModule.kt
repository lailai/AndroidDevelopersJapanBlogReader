package net.lailai.android.android_developers_japan_blog_reader.di

import net.lailai.android.android_developers_japan_blog_reader.MainViewModel
import net.lailai.android.android_developers_japan_blog_reader.data.repository.BlogRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// DI (Koin) の設定(アプリ系)
val appModule = module {
    single { BlogRepository(get()) }
    viewModel { MainViewModel(get()) }
}
