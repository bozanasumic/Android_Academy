package com.example.androidacademynotes

class EditViewModel(
    private val repository: NoteRepository = AppContainer.noteRepository
) {
    fun getNote(id: Int): Note? = repository.getNoteById(id)

    fun saveNote(id: Int, title: String, content: String) {
        if (id == -1) {
            repository.addNote(title, content)
        } else {
            repository.updateNote(id, title, content)
        }
    }
}