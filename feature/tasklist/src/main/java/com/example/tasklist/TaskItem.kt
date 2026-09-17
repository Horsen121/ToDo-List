package com.example.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.feature.tasklist.R
import com.example.ui.StatusChip
import com.example.ui.formatTaskDate

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onTakeInProgress: () -> Unit,
    onComplete: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            CardTitle(task.shortDescription)
            CardBody(task)
            CardFooter(
                task.status,
                onTakeInProgress,
                onComplete
            ) { showDeleteConfirmation = true }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(stringResource(R.string.alert_title)) },
            text = { Text(stringResource(R.string.alert_text)) },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteConfirmation = false }) { Text(stringResource(R.string.alert_action_delete)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) { Text(stringResource(R.string.alert_action_cancel)) }
            }
        )
    }
}

@Composable
private fun CardTitle(
    shortDescription: String,
    modifier: Modifier = Modifier
) {
    Text(shortDescription, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier.height(4.dp))
}

@Composable
private fun CardBody(
    task: Task,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        StatusChip(task.status)
        Spacer(modifier.width(8.dp))

        Text(
            formatTaskDate(task.createdAt),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Spacer(modifier.height(8.dp))
}

@Composable
private fun CardFooter(
    status: TaskStatus,
    onTakeInProgress: () -> Unit,
    onComplete: () -> Unit,
    onShowDeleteConfirmation: (Boolean) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (status == TaskStatus.NEW) {
            TextButton(onClick = onTakeInProgress) { Text(stringResource(R.string.task_action_get_to_work)) }
        }
        if (status == TaskStatus.IN_PROGRESS) {
            TextButton(onClick = onComplete) { Text(stringResource(R.string.task_action_set_to_done)) }
        }
        if (status == TaskStatus.NEW) {
            TextButton(onClick = { onShowDeleteConfirmation(true) }) { Text(stringResource(R.string.task_action_delete)) }
        }
    }
}