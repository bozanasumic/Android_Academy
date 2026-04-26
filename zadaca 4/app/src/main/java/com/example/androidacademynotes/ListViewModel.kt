package com.example.androidacademynotes

class ListViewModel(
    private val repository: NoteRepository = AppContainer.noteRepository
) {
    fun getNotes(): List<Note> = repository.getNotes()
}