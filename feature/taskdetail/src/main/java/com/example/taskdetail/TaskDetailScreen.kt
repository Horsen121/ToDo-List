package com.example.taskdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Task
import com.example.feature.taskdetail.R
import com.example.ui.StatusChip
import com.example.ui.formatTaskDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(viewModel: TaskDetailViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.screen_task_detail_top_bar)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.screen_task_detail_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (state) {
                TaskDetailUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                TaskDetailUiState.NotFound -> Text(
                    stringResource(R.string.screen_task_detail_not_found),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
                is TaskDetailUiState.Content -> ContentScreen((state as TaskDetailUiState.Content).task)
            }
        }
    }
}

@Composable
private fun ContentScreen(
    task: Task,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.padding(16.dp)
    ) {
        TaskTitle(task.shortDescription)
        TaskDescription(task.fullDescription)

        StatusChip(task.status)
        Spacer(Modifier.height(16.dp))

        TaskCreatingTime(task.createdAt)
    }
}

@Composable
private fun TaskTitle(
    shortDescription: String,
    modifier: Modifier = Modifier
) {
    Text(
        shortDescription,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun TaskDescription(
    fullDescription: String,
    modifier: Modifier = Modifier
) {
    Text(
        fullDescription,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun TaskCreatingTime(
    createdAt: Long,
    modifier: Modifier = Modifier
) {
    Text(
        stringResource(
            R.string.screen_task_detail_time_of_creating,
            formatTaskDate(createdAt)
        ),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}