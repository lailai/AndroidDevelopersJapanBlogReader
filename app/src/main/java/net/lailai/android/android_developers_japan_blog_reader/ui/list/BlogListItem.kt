package net.lailai.android.android_developers_japan_blog_reader.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.lailai.android.android_developers_japan_blog_reader.ui.theme.AndroidDevelopersJapanBlogReaderTheme
import net.lailai.android.android_developers_japan_blog_reader.usecase.param.BlogListData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BlogListItem(
    entry: BlogListData.Entry,
    onNavigateToBlogDetail: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = { onNavigateToBlogDetail(entry.link) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ListItem(
            leadingContent = {
                entry.imageUrl?.let { imageUrl ->
                    if (imageUrl == "dummy") {
                        Icon(
                            modifier = Modifier.size(56.dp),
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = ""
                        )
                    } else {
                        AsyncImage(
                            modifier = Modifier.size(56.dp),
                            model = imageUrl,
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            },
            headlineContent = {
                Text(
                    text = entry.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.JAPAN).format(entry.date),
                    maxLines = 1
                )
            }
        )
    }
}

@Preview(
    device = Devices.PIXEL,
    showSystemUi = false,
    showBackground = true
)
@Composable
fun BlogListItemPreview() {
    AndroidDevelopersJapanBlogReaderTheme {
        BlogListItem(
            BlogListData.Entry(
                "テストテストテストテストテストテストテストテストテストテストテストテストテスト",
                Date(),
                "dummy",
                "dummy"
            )
        )
    }
}
