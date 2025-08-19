package com.example.astratechpoststask.feature.post.presentation.screen.create

import android.R.id.message
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.astratechpoststask.core.event.UiEvent
import com.example.astratechpoststask.core.event.combinedEvent
import com.example.astratechpoststask.core.utils.getFileFromUri
import com.example.astratechpoststask.core.utils.rootModifier
import com.example.astratechpoststask.feature.post.presentation.viewmodel.BlogViewModel
import com.example.astratechpoststask.feature.post.presentation.viewmodel.event.BlogEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CreateBlogScreen(
    blogViewModel: BlogViewModel,
    snackBarHostState: SnackbarHostState,
    onCreateBlogButtonClicked: () -> Unit,
) {
    val blogState by blogViewModel.blogState.collectAsStateWithLifecycle()
    val createBlog = blogState.createBlog
    Column(
        modifier = rootModifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        Text(
            "Create Blog",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        CreateBlogForm(
            blogViewModel = blogViewModel,
            isLoading = blogState.isLoading,
            title = createBlog.title,
            onTitleChange = {
                blogViewModel.onEvent(BlogEvent.Input.CreateBlog.CreateBlogTitle(it))
            },
            content = createBlog.content,
            onContentValue = {
                blogViewModel.onEvent(BlogEvent.Input.CreateBlog.CreateBlogContent(it))
            },
            onImagePicked = {

                blogViewModel.onEvent(BlogEvent.Input.CreateBlog.CreateBlogPhoto(it))
            }
        )

    }
    CreateBlogEvent(
        blogEvent = blogViewModel.blogEvent,
        snackBarHostState = snackBarHostState,
        onCreateBlogButtonClicked = onCreateBlogButtonClicked
    )

}

@Composable
private fun CreateBlogForm(
    blogViewModel: BlogViewModel,
    isLoading: Boolean,
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentValue: (String) -> Unit,
    onImagePicked: (String) -> Unit,
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            val pathfile = getFileFromUri(context, it)
            pathfile?.let { file ->
                onImagePicked(file.absolutePath)
            }
        }
    }



    Column(
        modifier = Modifier.verticalScroll(
            rememberScrollState()
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(200.dp)
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Blog Image",
                    modifier = Modifier.heightIn(200.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Tap to pick an image", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = "title",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            isError = title.isBlank(),
            placeholder = {
                Text("Title cannot be empty")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "content",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        OutlinedTextField(
            value = content,
            onValueChange = onContentValue,
            isError = content.isBlank(),
            placeholder = {
                Text("Content cannot be empty")
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(150.dp)
                .padding(8.dp)
        )
        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                blogViewModel.onEvent(
                    BlogEvent.Click.CreateBlog
                )
            },
            enabled = title.isNotBlank() && content.isNotBlank() && imageUri != null && !isLoading,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            AnimatedContent(
                targetState = isLoading,
                label = "Loading Animation"
            ) { isLoading ->
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(24.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = "Create Blog",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

        }
    }
}

@Composable
fun CreateBlogEvent(
    blogEvent: Flow<UiEvent>,
    snackBarHostState: SnackbarHostState,
    onCreateBlogButtonClicked: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = blogEvent) {
        blogEvent.collectLatest { event ->
            Log.e("BlogEvent", "BlogEvent: $event")
            when (event) {
                is UiEvent.CombinedEvents -> {
                    combinedEvent(
                        event = event.event,
                        onShowMessage = { message ->
                            launch {
                                snackBarHostState.showSnackbar(
                                    message = message.asString(context)
                                )
                            }
                        },
                        onNavigate = { navEvent ->
                            when (navEvent) {
                                is UiEvent.NavEvent.HomeScreen -> {
                                    onCreateBlogButtonClicked()
                                }

                                else -> Unit
                            }
                        },
                    )
                }

                is UiEvent.ShowSnackBar -> {
                    launch {
                        snackBarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }

                else -> Unit
            }
        }

    }

}