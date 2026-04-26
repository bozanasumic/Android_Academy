package com.example.androidacademynotes

data class Note(
    val id: Int,
    var title: String,
    var content: String,
    val createdAt: Long = System.currentTimeMillis()
)