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
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.androidacademynotes.ui.theme.AndroidAcademyNotesTheme

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

    val listViewModel = remember { ListViewModel() }
    val editViewModel = remember { EditViewModel() }

    var refreshTrigger by remember { mutableStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {

        composable("list") {

            val notes = listViewModel.getNotes()

            NotesListScreen(
                notes = notes,
                onAddClick = {
                    navController.navigate("edit/-1")
                },
                onNoteClick = { id ->
                    navController.navigate("edit/$id")
                }
            )
        }

        composable(
            route = "edit/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->

            val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1

            val note = if (noteId != -1) editViewModel.getNote(noteId) else null

            EditNoteScreen(
                note = note,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { title, content ->
                    editViewModel.saveNote(noteId, title, content)
                    refreshTrigger++
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
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF7B61FF)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Notes",
                color = Color(0xFF7B61FF),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notes) { note ->
                    NoteCard(note = note) {
                        onNoteClick(note.id)
                    }
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
    var content by remember { mutableStateOf(note?.content ?: "") }

    Scaffold(
        topBar = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onSaveClick(title, content) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
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
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = note.title,
                color = Color(0xFF7B61FF),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.content,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}