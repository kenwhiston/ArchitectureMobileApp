package com.jassgroup.architecturemobileapp.ui.firebase.firestore

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para el demo de Cloud Firestore.
 *
 * Escucha cambios en tiempo real en la colección 'demo_notes' usando snapshot listeners.
 */
class FirestoreViewModel(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow<FirestoreUiState>(FirestoreUiState.Loading)
    val uiState: StateFlow<FirestoreUiState> = _uiState.asStateFlow()

    init {
        listenToNotes()
    }

    private fun listenToNotes() {
        firestore.collection("demo_notes")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = FirestoreUiState.Error(
                        error.localizedMessage ?: "Error al escuchar Firestore"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val notes = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(FirestoreNote::class.java)?.copy(id = doc.id)
                    }
                    _uiState.value = FirestoreUiState.Success(notes)
                }
            }
    }

    fun addNote(title: String, content: String) {
        if (title.isBlank()) return

        val newNote = mapOf(
            "title" to title.trim(),
            "content" to content.trim(),
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("demo_notes").add(newNote)
    }

    fun deleteNote(noteId: String) {
        firestore.collection("demo_notes").document(noteId).delete()
    }
}
