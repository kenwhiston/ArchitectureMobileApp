package com.jassgroup.architecturemobileapp.ui.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Pantalla para Agregar o Editar una Tarea.
 *
 * En la arquitectura MVVM:
 * - Esta vista es reutilizable para las pantallas 2 (Agregar) y 3 (Editar).
 * - Observa el [AddEditUiState] expuesto por el [AddEditViewModel].
 * - [LaunchedEffect] reacciona a eventos como la carga del ID de la tarea o la confirmación del guardado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: Int,
    viewModel: AddEditViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Si taskId > 0, cargamos los datos de la tarea existente para editar
    LaunchedEffect(taskId) {
        if (taskId > 0) {
            viewModel.loadTask(taskId)
        }
    }

    // Cuando la tarea se guarda con éxito, se navega automáticamente hacia atrás
    LaunchedEffect(state.isTaskSaved) {
        if (state.isTaskSaved) {
            onNavigateBack()
        }
    }

    val titleText = if (state.isEditing) "Editar Tarea" else "Nueva Tarea"

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(titleText) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Campo Título
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { viewModel.onTitleChanged(it) },
                    label = { Text("Título de la tarea *") },
                    isError = state.isTitleError,
                    supportingText = {
                        if (state.isTitleError && state.titleErrorMessage != null) {
                            Text(
                                text = state.titleErrorMessage!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Descripción
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { viewModel.onDescriptionChanged(it) },
                    label = { Text("Descripción (opcional)") },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Guardar / Actualizar
                Button(
                    onClick = { viewModel.saveTask() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(if (state.isEditing) "Actualizar Tarea" else "Guardar Tarea")
                }
            }
        }
    }
}
