package com.example.taskdetail

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature.taskdetail.R
import com.example.ui.asString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    viewModel: AddTaskViewModel = hiltViewModel(),
    onTaskCreated: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var shortDescription by remember { mutableStateOf("") }
    var fullDescription by remember { mutableStateOf("") }

    LaunchedEffect(state ) {
        if ((state is AddTaskUiState.Saving)) onTaskCreated()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.screen_add_task_new_task)) })
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            TaskTitle(
                shortDescription,
                { shortDescription = it },
                state,
                context
            )

            TaskDescription(
                fullDescription,
                { fullDescription = it },
                state,
            )

            TaskSaveButton(
                shortDescription,
                fullDescription,
                viewModel,
                state
            )
        }
    }
}

@Composable
private fun TaskTitle(
    shortDescription: String,
    onShortDescriptionChange: (String) -> Unit,
    state: AddTaskUiState,
    context: Context,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = shortDescription,
        onValueChange = { onShortDescriptionChange(it) },
        label = { Text(stringResource(R.string.screen_add_task_short_desc)) },
        isError = (state is AddTaskUiState.Error),
        enabled = state is AddTaskUiState.Editing,
        modifier = modifier.fillMaxWidth()
    )
    val errorMessage = (state as? AddTaskUiState.Error)?.message?.asString(context)
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun TaskDescription(
    fullDescription: String,
    onFullDescriptionChange: (String) -> Unit,
    state: AddTaskUiState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = fullDescription,
        onValueChange = { onFullDescriptionChange(it) },
        label = { Text(stringResource(R.string.screen_add_task_full_desc)) },
        enabled = state is AddTaskUiState.Editing,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
    )
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun TaskSaveButton(
    shortDescription: String,
    fullDescription: String,
    viewModel: AddTaskViewModel,
    state: AddTaskUiState,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { viewModel.onSave(shortDescription, fullDescription) },
        enabled = state is AddTaskUiState.Editing,
        modifier = modifier.fillMaxWidth()
    ) {
        if (state is AddTaskUiState.Saving) {
            CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
        } else {
            Text(stringResource(R.string.screen_add_task_save))
        }
    }
}