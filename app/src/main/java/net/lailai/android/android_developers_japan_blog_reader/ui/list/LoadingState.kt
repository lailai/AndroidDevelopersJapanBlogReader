package net.lailai.android.android_developers_japan_blog_reader.ui.list

sealed class LoadingState {
    data object None : LoadingState()
    data object Processing : LoadingState()
    data object Success : LoadingState()
    data class Error(val message: String) : LoadingState()
}
