package com.example.astratechpoststask.feature.post.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astratechpoststask.R
import com.example.astratechpoststask.core.error.DomainError
import com.example.astratechpoststask.core.event.UiEvent
import com.example.astratechpoststask.core.ui_text.UiText
import com.example.astratechpoststask.core.ui_text.asUiTextOrDefault
import com.example.astratechpoststask.core.utils.Result
import com.example.astratechpoststask.core.utils.performUseCaseOperation
import com.example.astratechpoststask.feature.post.domain.entity.BlogCreateEntity
import com.example.astratechpoststask.feature.post.domain.entity.BlogUpdateEntity
import com.example.astratechpoststask.feature.post.domain.repository.BlogRepository
import com.example.astratechpoststask.feature.post.presentation.viewmodel.event.BlogEvent
import com.example.astratechpoststask.feature.post.presentation.viewmodel.state.BlogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(
    private val repository: BlogRepository
) : ViewModel() {
    private val _blogEvent = Channel<UiEvent>()
    val blogEvent = _blogEvent.receiveAsFlow()
    private val _blogState = MutableStateFlow(BlogState())
    val blogState: StateFlow<BlogState> = _blogState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = BlogState()
    )

    init {
        performUseCaseOperation(
            scope = viewModelScope,
            useCase = {
                repository.getPosts()
            },
            onSuccess = { showBlogs ->
                _blogState.update { it.copy(showBlogs = showBlogs) }
            },
            onError = { error ->
                _blogEvent.send(UiEvent.ShowSnackBar(error.asUiTextOrDefault()))
            },
        )
    }

    fun onEvent(event: BlogEvent) {
        when (event) {
            is BlogEvent.Click.CreateBlog -> {
                createBlog()
            }

            is BlogEvent.Click.DeleteBlog -> {
                deleteBlog()
            }

            is BlogEvent.Click.ShowBlog -> {
                showBlog()
            }

            is BlogEvent.Click.UpdateBlog -> {
                updateBlog()
            }

            is BlogEvent.Input.BlogId -> {
                _blogState.update { it.copy(id = event.id) }
            }

            is BlogEvent.Input.CreateBlog.CreateBlogTitle -> {
                _blogState.update { it.copy(createBlog = it.createBlog.copy(title = event.title)) }
            }

            is BlogEvent.Input.CreateBlog.CreateBlogContent -> {
                _blogState.update { it.copy(createBlog = it.createBlog.copy(content = event.content)) }
            }

            is BlogEvent.Input.CreateBlog.CreateBlogPhoto -> {
                _blogState.update { it.copy(createBlog = it.createBlog.copy(photo = event.photo)) }
            }

            is BlogEvent.Input.UpdateBlog.UpdateBlogId -> {
                _blogState.update { it.copy(updateBlog = it.updateBlog.copy(id = event.id)) }
            }

            is BlogEvent.Input.UpdateBlog.UpdateBlogTitle -> {
                _blogState.update { it.copy(updateBlog = it.updateBlog.copy(title = event.title)) }
            }

            is BlogEvent.Input.UpdateBlog.UpdateBlogContent -> {
                _blogState.update { it.copy(updateBlog = it.updateBlog.copy(content = event.content)) }
            }

            is BlogEvent.Input.UpdateBlog.UpdateBlogPhoto -> {
                _blogState.update { it.copy(updateBlog = it.updateBlog.copy(photo = event.photo)) }
            }

        }
    }

    private fun createBlog() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            _blogState.update { it.copy(isLoading = true) }
            val createPostParams = blogState.value.createBlog
            val blogsParams = BlogCreateEntity(
                title = createPostParams.title,
                content = createPostParams.content,
                photo = createPostParams.photo,
            )
            repository.createPost(blogsParams)
        },
        onSuccess = { blog ->

            _blogEvent.send(
                UiEvent.CombinedEvents(
                    listOf(
                        UiEvent.ShowSnackBar(
                            UiText.from(R.string.successful_create_blog)
                        ),
                        UiEvent.NavEvent.HomeScreen
                    )
                )
            )

            _blogState.update {
                it.copy(
                    showBlogs = it.showBlogs.plus(blog), isLoading = false
                )

            }
        },
        onError = {
            _blogEvent.send(UiEvent.ShowSnackBar(it.asUiTextOrDefault()))
        }
    )

    private fun deleteBlog() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            val id = blogState.value.id
            repository.deletePostById(id)
        },
        onSuccess = {
            val id = blogState.value.id

            _blogState.update { state ->
                state.copy(showBlogs = state.showBlogs.filterNot { blog ->
                    blog.id == id
                })
            }
            _blogEvent.send(
                UiEvent.CombinedEvents(
                    listOf(
                        UiEvent.ShowSnackBar(UiText.from(it.message)),
                        UiEvent.NavEvent.HomeScreen,
                    )
                )
            )
        },
        onError = {
            _blogEvent.send(UiEvent.ShowSnackBar(it.asUiTextOrDefault()))

        }
    )

    private fun showBlog() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            val id = blogState.value.id
            repository.getPostById(id)
        },
        onSuccess = { blog ->
            _blogState.update { it.copy(showBlog = blog) }
            _blogEvent.send(UiEvent.NavEvent.BlogDetailsScreen(blog.id))
        },
        onError = {
            _blogEvent.send(UiEvent.ShowSnackBar(it.asUiTextOrDefault()))

        },
    )

    private fun updateBlog() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            _blogState.update { it.copy(isLoading = true) }
            val updateBlog = _blogState.value.updateBlog
            val id = updateBlog.id
            if (id == null) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)
            }
            val blogEntity = BlogUpdateEntity(
                id = updateBlog.id,
                title = updateBlog.title,
                content = updateBlog.content,
                photo = updateBlog.photo,
            )
            repository.updatePost(blogEntity)
        },
        onSuccess = { blog ->
            _blogEvent.send(
                UiEvent.CombinedEvents(
                    listOf(
                        UiEvent.ShowSnackBar(
                            UiText.from(R.string.success_update_blog)
                        ),
                        UiEvent.NavEvent.HomeScreen

                    )
                )
            )
            _blogState.update {
                it.copy(
                    isLoading = false,
                    showBlogs = it.showBlogs.map { old ->
                        if (old.id == blog.id) blog else old
                    }
                )

            }

        },
        onError = {
            _blogEvent.send(UiEvent.ShowSnackBar(it.asUiTextOrDefault()))
        }
    )

}