package com.example.astratechpoststask.feature.post.presentation.viewmodel.event

import com.example.astratechpoststask.feature.post.presentation.viewmodel.state.CreateBlog
import com.example.astratechpoststask.feature.post.presentation.viewmodel.state.DeleteBlog
import com.example.astratechpoststask.feature.post.presentation.viewmodel.state.UpdateBlog

sealed class BlogEvent {
    sealed class Input : BlogEvent() {
        data class CreateBlogState(val createBlog: CreateBlog) : Input()
        data class UpdateBlogState(val updateBlog: UpdateBlog) : Input()
        data class BlogId(val id: Int): Input()
    }
    sealed class Click: BlogEvent(){
        data object CreateBlog: Click()
        data object ShowBlog: Click()
        data object UpdateBlog:Click()
        data object DeleteBlog: Click()
    }


}