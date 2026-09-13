package com.example.ui

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.domain.model.TaskStatus
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

@Composable
fun StatusChip(status: TaskStatus) {
    val (label, color) = when (status) {
        TaskStatus.NEW -> "Новая" to MaterialTheme.colorScheme.primary
        TaskStatus.IN_PROGRESS -> "В работе" to MaterialTheme.colorScheme.tertiary
        TaskStatus.DONE -> "Выполнена" to MaterialTheme.colorScheme.secondary
    }
    AssistChip(
        onClick = {},
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(labelColor = color)
    )
}

fun formatTaskDate(
    timestamp: Long,
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault()
): String =
    Instant.ofEpochMilli(timestamp)
        .atZone(zoneId)
        .format(formatter.withLocale(locale))