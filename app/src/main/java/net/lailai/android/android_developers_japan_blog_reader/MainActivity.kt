package net.lailai.android.android_developers_japan_blog_reader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.lailai.android.android_developers_japan_blog_reader.ui.detail.BlogDetailScreen
import net.lailai.android.android_developers_japan_blog_reader.ui.list.BlogListScreen
import net.lailai.android.android_developers_japan_blog_reader.ui.theme.AndroidDevelopersJapanBlogReaderTheme
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Date

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidDevelopersJapanBlogReaderTheme {
                val pullToRefreshState = rememberPullToRefreshState()
                Scaffold(
                    modifier = Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection),
                    topBar = {
                        CenterAlignedTopAppBar(title = { Text(text = "TopAppBar") })
                    },
                    bottomBar = {
                        BottomAppBar {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(onClick = {}) {
                                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                                }
                                IconButton(onClick = {}) {
                                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                                }
                                IconButton(onClick = {}) {
                                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                                }
                                IconButton(onClick = {}) {
                                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    MyApp(
                        pullToRefreshState = pullToRefreshState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    pullToRefreshState: PullToRefreshState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BlogList.toString(),
    ) {
        composable(
            route = BlogList.toString()
        ) {
            BlogListScreen(
                pullToRefreshState = pullToRefreshState,
                onNavigateToBlogDetail = { link ->
                    val encodedUrl = URLEncoder.encode(link, StandardCharsets.UTF_8.toString())
                    navController.navigate(route = "${BlogDetail}/$encodedUrl")
                }
            )
        }
        composable(
            route = "${BlogDetail}/{link}",
            arguments = listOf(navArgument("link") { type = NavType.StringType })
        ) { backStackEntry ->
            BlogDetailScreen(
                url = backStackEntry.arguments?.getString("link").orEmpty(),
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    device = Devices.PIXEL,
    showSystemUi = true,
    showBackground = true
)
@Composable
fun BlogListPreview() {
    AndroidDevelopersJapanBlogReaderTheme {
        BlogListScreen(
            remember {
                mutableStateOf(
                    BlogListData(
                        List(10) {
                            BlogListData.Entry("テスト", Date(), "dummy", "dummy")
                        }
                    )
                )
            }
        )
    }
}
