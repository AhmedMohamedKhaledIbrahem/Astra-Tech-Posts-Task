package com.example.astratechpoststask.feature.post.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astratechpoststask.core.error.DomainError
import com.example.astratechpoststask.core.event.UiEvent
import com.example.astratechpoststask.core.ui_text.UiText
import com.example.astratechpoststask.core.ui_text.asUiTextOrDefault
import com.example.astratechpoststask.core.utils.Result
import com.example.astratechpoststask.core.utils.performUseCaseOperation
import com.example.astratechpoststask.feature.post.domain.entity.BlogEntity
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
                _blogEvent.send(UiEvent.NavEvent.HomeScreen)
            },
            onError = { error ->
                _blogEvent.send(UiEvent.ShowSnackBar(error.asUiTextOrDefault()))
            },
        )
    }

    fun onEvent(event: BlogEvent) {
        when (event) {
            is BlogEvent.Click.CreateBlog -> createBlog()
            is BlogEvent.Click.DeleteBlog -> deleteBlog()
            is BlogEvent.Click.ShowBlog -> showBlog()
            is BlogEvent.Click.UpdateBlog -> TODO()
            is BlogEvent.Input.CreateBlogState -> _blogState.update { it.copy(createBlog = event.createBlog) }
            is BlogEvent.Input.BlogId -> _blogState.update { it.copy(id = event.id) }
            is BlogEvent.Input.UpdateBlogState -> _blogState.update { it.copy(updateBlog = event.updateBlog) }
        }
    }

    fun createBlog() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            _blogState.update { it.copy(isLoading = true) }
            val createPostParams = blogState.value.createBlog
                ?: return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)

            if (createPostParams.title.isNullOrBlank()) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)
            }
            if (createPostParams.content.isNullOrBlank()) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)
            }
            if (createPostParams.photo.isNullOrBlank()) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)
            }
            if (createPostParams.create.isNullOrBlank()) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)
            }
            if (createPostParams.update.isNullOrBlank()) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)
            }
            val blogsParams = BlogEntity(
                title = createPostParams.title,
                content = createPostParams.content,
                photo = createPostParams.photo,
                created = createPostParams.create,
                updated = createPostParams.update

            )
            repository.createPost(blogsParams)
        },
        onSuccess = { blog ->
            _blogState.update { it.copy(isLoading = false) }
            _blogState.update {
                it.copy(
                    showBlogs = it.showBlogs?.plus(blog)
                )

            }
            _blogEvent.send(UiEvent.ShowSnackBar(UiText.from("Successful create blog")))
        },
        onError = {
            _blogEvent.send(UiEvent.ShowSnackBar(it.asUiTextOrDefault()))
        }
    )

    fun deleteBlog() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            val id = blogState.value.id
            if (id == null) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)
            }
            repository.deletePostById(id)
        },
        onSuccess = {
            val id = blogState.value.id
            if (id != null) {
                _blogEvent.send(UiEvent.ShowSnackBar(UiText.from(it.message)))
                _blogState.update { state ->
                    state.copy(showBlogs = state.showBlogs?.filterNot { blog ->
                        blog.id == id
                    })
                }
            }
        },
        onError = {
            _blogEvent.send(UiEvent.ShowSnackBar(it.asUiTextOrDefault()))

        }
    )
    fun showBlog() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            val id = blogState.value.id
            if (id == null) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)
            }
            repository.getPostById(id)
        },
        onSuccess = { blog->
            _blogState.update { it.copy(showBlog = blog ) }
            _blogEvent.send(UiEvent.NavEvent.BlogDetailsScreen(blog.id))
        },
        onError = {
            _blogEvent.send(UiEvent.ShowSnackBar(it.asUiTextOrDefault()))

        },
    )
    fun updateBlog()=performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            val updateBlog = _blogState.value.updateBlog
            val id = updateBlog?.id
            if (id == null) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN_ERROR)
            }
            BlogEntity(
                id = updateBlog.id,
                title = updateBlog?.title,
                content = updateBlog?.content,
                photo = updateBlog?.photo,
                updated = updateBlog?.update,
                created = updateBlog?.update
            )
            repository.updatePost()
        },
        onSuccess = {},
        onError = {}
    )

}