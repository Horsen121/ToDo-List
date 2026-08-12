package com.example.taskdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    viewModel: AddTaskViewModel = hiltViewModel(),
    onTaskCreated: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var shortDescription by remember { mutableStateOf("") }
    var fullDescription by remember { mutableStateOf("") }

    LaunchedEffect(state ) {
        if ((state is AddTaskUiState.Saving)) onTaskCreated()
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Новая задача") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxWidth()) {
            OutlinedTextField(
                value = shortDescription,
                onValueChange = { shortDescription = it },
                label = { Text("Краткое описание") },
                isError = (state as? AddTaskUiState.Error)?.error?.isEmpty() ?: false,
                enabled = state !is AddTaskUiState.Saving,
                modifier = Modifier.fillMaxWidth()
            )
            (state as? AddTaskUiState.Error)?.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = fullDescription,
                onValueChange = { fullDescription = it },
                label = { Text("Полное описание") },
                enabled = state !is AddTaskUiState.Saving,
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onSave(shortDescription, fullDescription) },
                enabled = state !is AddTaskUiState.Saving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state is AddTaskUiState.Saving) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Сохранить")
                }
            }
        }
    }
}