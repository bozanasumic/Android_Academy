package com.example.androidacademynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.androidacademynotes.ui.theme.AndroidAcademyNotesTheme
import androidx.compose.ui.graphics.Color

data class Note(
    val id: Int,
    val title: String,
    val description: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAcademyNotesTheme {
                NotesApp()
            }
        }
    }
}

@Composable
fun NotesApp() {
    val navController = rememberNavController()

    var notes by remember {
        mutableStateOf(
            listOf(
                Note(1, "Note 1", "This is my first note."),
                Note(2, "Note 2", "This is my second note."),
                Note(3, "Android Academy", "Catch up with Compose homework."),
                Note(4, "Kotlin", "Practice data classes and state."),
                Note(5, "Reminder", "Do not panic. Gradle is dramatic.")
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            NotesListScreen(
                notes = notes,
                onAddClick = {
                    navController.navigate("edit/-1")
                },
                onNoteClick = { noteId ->
                    navController.navigate("edit/$noteId")
                }
            )
        }

        composable(
            route = "edit/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
            val existingNote = notes.find { it.id == noteId }

            EditNoteScreen(
                note = existingNote,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = { title, description ->
                    if (existingNote == null) {
                        val newId = (notes.maxOfOrNull { it.id } ?: 0) + 1
                        notes = notes + Note(newId, title, description)
                    } else {
                        notes = notes.map {
                            if (it.id == existingNote.id) {
                                it.copy(title = title, description = description)
                            } else {
                                it
                            }
                        }
                    }

                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun NotesListScreen(
    notes: List<Note>,
    onAddClick: () -> Unit,
    onNoteClick: (Int) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick,containerColor = Color(0xFF7B61FF)) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add note"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TitleText(text = "Notes")

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notes) { note ->
                    NoteCard(
                        note = note,
                        onClick = { onNoteClick(note.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun EditNoteScreen(
    note: Note?,
    onBackClick: () -> Unit,
    onSaveClick: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var description by remember { mutableStateOf(note?.description ?: "") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                maxLines = 6
            )

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = "Save",
                onClick = {
                    if (title.isNotBlank() || description.isNotBlank()) {
                        onSaveClick(title, description)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDE7F6)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TitleText(text = note.title)
            Spacer(modifier = Modifier.height(8.dp))
            DescriptionText(text = note.description)
        }
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        color = Color(0xFF7B61FF),
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun DescriptionText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}