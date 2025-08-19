package com.example.astratechpoststask.feature.post.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.astratechpoststask.core.event.UiEvent
import com.example.astratechpoststask.core.utils.rootModifier
import com.example.astratechpoststask.feature.post.presentation.viewmodel.BlogViewModel
import com.example.astratechpoststask.feature.post.presentation.viewmodel.event.BlogEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    blogViewModel: BlogViewModel,
    snackBarHostState: SnackbarHostState,
    onBlogClick: (Int) -> Unit
) {
    val blogState by blogViewModel.blogState.collectAsStateWithLifecycle()
    Column(
        modifier = rootModifier,
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = blogState.showBlogs,
                key = { blog -> blog.id }
            ) { blog ->
                BlogCard(
                    imageUrl = blog.photo,
                    title = blog.title,
                    onBlogClick = {
                        blogViewModel.onEvent(
                            BlogEvent.Input.BlogId(blog.id)
                        )
                        blogViewModel.onEvent(
                            BlogEvent.Click.ShowBlog
                        )

                    }
                )
            }
        }

    }
    BlogEvent(
        blogEvent = blogViewModel.blogEvent,
        snackBarHostState = snackBarHostState,
        onBlogClick = onBlogClick
    )
}

@Composable
fun BlogCard(
    imageUrl: String,
    title: String,
    onBlogClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable {

                onBlogClick()

            },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Blog Image",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BlogEvent(
    blogEvent: Flow<UiEvent>,
    snackBarHostState: SnackbarHostState,
    onBlogClick: (Int) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(blogEvent) {
        blogEvent.collectLatest { event ->
            Log.e("BlogEvent", "BlogEvent: $event")

            when (event) {
                is UiEvent.ShowSnackBar -> {
                    launch {
                        snackBarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }

                }

                is UiEvent.NavEvent.BlogDetailsScreen -> {
                    onBlogClick(event.id)
                }

                else -> Unit
            }
        }
    }
}