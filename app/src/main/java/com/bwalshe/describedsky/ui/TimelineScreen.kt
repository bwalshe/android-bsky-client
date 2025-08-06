package com.bwalshe.describedsky.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsFeedViewPost

@Composable
fun TimelineScreen(
    posts: List<FeedDefsFeedViewPost>,
    refresh: () -> Unit,
    logout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row {
            Button(onClick=refresh) {Text("Refresh") }
            Button(onClick=logout) {Text("Logout")}
        }
        Text("Posts")
        LazyColumn {
            items(posts) {
                PostCard(it)
            }
        }
    }
}

@Composable
fun PostCard(post: FeedDefsFeedViewPost) {
    Card {
        Text("${post.post.author?.handle ?: "unknown"} said:")
        Text(post.post.record?.asFeedPost?.text ?: "nothing")
    }
}