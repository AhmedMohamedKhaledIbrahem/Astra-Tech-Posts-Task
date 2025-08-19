package com.example.astratechpoststask.feature.post.presentation.viewmodel.event

import com.example.astratechpoststask.feature.post.presentation.viewmodel.state.CreateBlog
import com.example.astratechpoststask.feature.post.presentation.viewmodel.state.DeleteBlog
import com.example.astratechpoststask.feature.post.presentation.viewmodel.state.UpdateBlog

sealed class BlogEvent {
    sealed class Input : BlogEvent() {
        sealed class CreateBlog : Input(){
            data class CreateBlogTitle(val title: String) : CreateBlog()
            data class CreateBlogContent(val content: String) : CreateBlog()
            data class CreateBlogPhoto(val photo: String) : CreateBlog()
        }
        sealed class UpdateBlog : Input(){
            data class UpdateBlogId(val id: Int) : UpdateBlog()
            data class UpdateBlogTitle(val title: String) : UpdateBlog()
            data class UpdateBlogContent(val content: String) : UpdateBlog()
            data class UpdateBlogPhoto(val photo: String) : UpdateBlog()
        }

        data class BlogId(val id: Int): Input()
    }
    sealed class Click: BlogEvent(){
        data object CreateBlog: Click()
        data object ShowBlog: Click()
        data object UpdateBlog:Click()
        data object DeleteBlog: Click()
    }


}