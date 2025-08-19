package com.example.astratechpoststask.feature.post.presentation.screen.details

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.astratechpoststask.core.event.UiEvent
import com.example.astratechpoststask.core.event.combinedEvent
import com.example.astratechpoststask.core.utils.getFileFromUri
import com.example.astratechpoststask.core.utils.rootModifier
import com.example.astratechpoststask.feature.post.presentation.screen.details.compose.BlogEditDialog
import com.example.astratechpoststask.feature.post.presentation.viewmodel.BlogViewModel
import com.example.astratechpoststask.feature.post.presentation.viewmodel.event.BlogEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun DetailsScreenRoot(
    blogViewModel: BlogViewModel,
    snackbarHostState: SnackbarHostState,
    onDeleteButtonClicked: () -> Unit,
    blogId: Int
) {
    val blogState by blogViewModel.blogState.collectAsStateWithLifecycle()
    val showBlog = blogState.showBlog

    Column(
        modifier = rootModifier
    ) {
        if (showBlog != null)
            BlogDetails(
                isLoading = blogState.isLoading,
                imageUrl = showBlog.photo,
                title = showBlog.title,
                content = showBlog.content,
                createdAt = showBlog.created,
                updatedAt = showBlog.updated,
                onDeleteConfirm = {
                    blogViewModel.onEvent(
                        BlogEvent.Click.DeleteBlog
                    )
                },
                onTitleChange = {
                    blogViewModel.onEvent(
                        BlogEvent.Input.UpdateBlog.UpdateBlogTitle(it)
                    )
                },
                titleUpdate = blogState.updateBlog.title?:showBlog.title,
                onContentValue = {
                    blogViewModel.onEvent(
                        BlogEvent.Input.UpdateBlog.UpdateBlogContent(it)
                    )
                },
                contentUpdate = blogState.updateBlog.content ?:showBlog.content ,
                onImagePicked = {
                    blogViewModel.onEvent(
                        BlogEvent.Input.UpdateBlog.UpdateBlogPhoto(it)
                    )
                },
                onClick = {
                    blogViewModel.onEvent(
                        BlogEvent.Input.UpdateBlog.UpdateBlogId(blogId)
                    )
                    blogViewModel.onEvent(
                        BlogEvent.Click.UpdateBlog
                    )
                }
            )
    }
    DetailsEvent(
        blogEvent = blogViewModel.blogEvent,
        snackbarHostState = snackbarHostState,
        onDeleteButtonClicked = onDeleteButtonClicked
    )
}

@Composable
fun BlogDetails(
    isLoading: Boolean,
    imageUrl: String,
    title: String,
    titleUpdate: String?,
    content: String,
    contentUpdate: String?,
    createdAt: String,
    updatedAt: String,
    onTitleChange: (String) -> Unit,
    onContentValue: (String) -> Unit,
    onImagePicked: (String) -> Unit,
    onClick: () -> Unit,
    onDeleteConfirm: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    LaunchedEffect(isLoading) {
        if (!isLoading) {
            showDeleteDialog = false
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                CompactIconButton(
                    onClick = { showEditDialog = true },
                    icon = Icons.Default.Edit,
                    tint = MaterialTheme.colorScheme.primary
                )
                CompactIconButton(
                    onClick = { showDeleteDialog = true },
                    icon = Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.error
                )

            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Created: $createdAt",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Updated: $updatedAt",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Blog Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 390.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Delete Blog") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteConfirm()
                    },
                    enabled = !isLoading
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Gray,
                        containerColor = Color.Transparent
                    ),
                    enabled = !isLoading
                ) {
                    Text("No")
                }
            }
        )


    }
    if (showEditDialog) {
        BlogEditDialog(
            isLoading = isLoading,
            title = titleUpdate,
            onTitleChange = onTitleChange,
            content = contentUpdate,
            onContentValue = onContentValue,
            onImagePicked = onImagePicked,
            onClick = onClick,
            onDismiss = {
                showEditDialog = false
            },
        )
    }
}


@Composable
fun DetailsEvent(
    blogEvent: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState,
    onDeleteButtonClicked: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(blogEvent) {
        blogEvent.collectLatest { event ->
            when (event) {
                is UiEvent.CombinedEvents -> {
                    combinedEvent(
                        event = event.event,
                        onShowMessage = { message ->
                            launch {
                                snackbarHostState.showSnackbar(
                                    message = message.asString(context)
                                )
                            }
                        },
                        onNavigate = { navEvent ->
                            when (navEvent) {
                                is UiEvent.NavEvent.HomeScreen -> {
                                    onDeleteButtonClicked()
                                }

                                else -> Unit
                            }
                        },
                    )
                }

                is UiEvent.ShowSnackBar -> {
                    launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun CompactIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    size: Dp = 36.dp,
    tint: Color
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint
        )
    }
}
