package com.example.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature.tasklist.R
import com.example.ui.asString

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    onTaskClick: (String) -> Unit,
    onAddClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TaskListEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.screen_action_create_task))
            }
        }
    ) { padding ->
        when(state) {
            is TaskListUiState.Loading -> LoadingScreen(padding)
            is TaskListUiState.Error -> ErrorScreen(padding, state as TaskListUiState.Error)
            is TaskListUiState.Success -> SuccessScreen(
                padding,
                state as TaskListUiState.Success,
                onTaskClick,
                viewModel
            )
        }
    }
}

@Composable
private fun LoadingScreen(
    padding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorScreen(
    padding: PaddingValues,
    state: TaskListUiState.Error,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.screen_text_empty_task_list, state.message))
    }
}

@Composable
private fun SuccessScreen(
    padding: PaddingValues,
    state: TaskListUiState.Success,
    onTaskClick: (String) -> Unit,
    viewModel: TaskListViewModel,
    modifier: Modifier = Modifier
) {
    if (state.tasks.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.screen_text_empty_task_list))
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onClick = { onTaskClick(task.id) },
                    onTakeInProgress = { viewModel.onTakeInProgress(task) },
                    onComplete = { viewModel.onComplete(task) },
                    onDelete = { viewModel.onDelete(task) }
                )
            }
        }
    }
}