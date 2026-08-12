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
import androidx.compose.ui.unit.dp
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
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
            Text(task.shortDescription, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusChip(task.status)
                Spacer(Modifier.width(8.dp))
                Text(
                    formatTaskDate(task.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (task.status == TaskStatus.NEW) {
                    TextButton(onClick = onTakeInProgress) { Text("Взять в работу") }
                }
                if (task.status == TaskStatus.IN_PROGRESS) {
                    TextButton(onClick = onComplete) { Text("Выполнить") }
                }
                if (task.status == TaskStatus.NEW) {
                    TextButton(onClick = { showDeleteConfirmation = true }) { Text("Удалить") }
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Удалить задачу?") },
            text = { Text("Действие необратимо.") },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteConfirmation = false }) { Text("Удалить") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) { Text("Отмена") }
            }
        )
    }
}
