package com.example.androidacademynotes

class NoteRepository {
    private val notes = mutableListOf(
        Note(1, "Note 1", "This is my first note."),
        Note(2, "Note 2", "This is my second note."),
        Note(3, "Android Academy", "Catching up with homework.")
    )

    fun getNotes(): List<Note> = notes

    fun getNoteById(id: Int): Note? = notes.find { it.id == id }

    fun addNote(title: String, content: String) {
        val newId = (notes.maxOfOrNull { it.id } ?: 0) + 1
        notes.add(Note(newId, title, content))
    }

    fun updateNote(id: Int, title: String, content: String) {
        val note = getNoteById(id)
        note?.title = title
        note?.content = content
    }
}

object AppContainer {
    val noteRepository: NoteRepository by lazy {
        NoteRepository()
    }
}