package com.bwalshe.describedsky.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bwalshe.describedsky.R
import com.bwalshe.describedsky.data.AuthorInfo
import com.bwalshe.describedsky.data.BlueSkyEmbedding
import com.bwalshe.describedsky.data.BlueSkyPost

const val TAG = "TimeLineScreen"

@Composable
fun TimelineScreen(
    posts: List<BlueSkyPost>,
    modifier: Modifier = Modifier,
    refresh: () -> Unit = {},
    logout: () -> Unit = {},
) {
    Column(modifier) {
        Text(
            "Timeline Posts:",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        LazyColumn {
            items(posts) { item ->
                PostCard(
                    post = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                )
            }
        }
    }
    ButtonRow(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.Bottom,
        refresh = refresh,
        logout = logout
    )

}

@Composable
fun ButtonRow(
    modifier: Modifier,
    verticalAlignment: Alignment.Vertical,
    refresh: () -> Unit,
    logout: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment
    ) {
        Button(
            onClick = refresh,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Refresh", fontSize = 10.sp)
        }
        Button(
            onClick = logout,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Logout", fontSize = 10.sp)
        }
    }
}

@Composable
fun PostCard(
    post: BlueSkyPost,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        val avatarUrl = post.author.avatarLink
        Log.d(TAG, "avatar url is $avatarUrl")
        Row {
            AsyncImage(
                model = avatarUrl,
                modifier = Modifier
                    .size(32.dp)
                    .clip(MaterialTheme.shapes.small),
                placeholder = painterResource(R.drawable.default_avatar),
                contentDescription = "avatar image",
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier.padding(
                    horizontal = 5.dp,
                    vertical = 2.dp
                )
            ) {
                PostHeader(post, Modifier.padding(bottom = 2.dp))
                PostContents(
                    post,
                )
            }
        }
    }
}

@Composable
fun PostContents(
    post: BlueSkyPost,
) {
    Text(post.text ?: "[text not found]")
    if(post.embedding != null) {
        PostEmbedding(post.embedding)
    }
}

@Composable
fun PostEmbedding(
    embedding: BlueSkyEmbedding
) {
    when (embedding) {
        is BlueSkyEmbedding.Images -> {
            val image = embedding.images[0]

            AsyncImage(
                model = image.link,
                modifier = Modifier.fillMaxWidth(),
                placeholder = painterResource(R.drawable.missing_image),
                contentDescription = image.altText,
                contentScale = ContentScale.Crop,
            )
        }

        is BlueSkyEmbedding.External -> {
            Card {
                Column {
                    AsyncImage(
                        model = embedding.thumbnailLink,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = painterResource(R.drawable.missing_image),
                        contentDescription = embedding.description,
                        contentScale = ContentScale.Crop
                    )
                    if (embedding.description != null)
                        Text(embedding.description)
                }
            }
        }
    }
}

@Composable
fun PostHeader(post: BlueSkyPost, modifier: Modifier) {
    val author = post.author
    Row(modifier = modifier) {
        if (author.name != null) {
            Text(author.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(1.dp))
        }
        Text("@${author.handle}", fontWeight = FontWeight.Light)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeline() {
    val author = AuthorInfo(
        did = "did:asdfasdfasf",
        handle = "person.example.com",
        name = "Some Person ",
        avatarLink = "https://example.com/avatar.jpg",
    )
    val longText =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    val shortText = "Let's keep this brief."
    val posts = listOf(
        BlueSkyPost(author = author, text = shortText),
        BlueSkyPost(author = author, text = longText),
        BlueSkyPost(author = author, text = shortText)
    )
    TimelineScreen(posts)
}