package com.ghosttalk.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.ghosttalk.core.utils.DateTimeUtils
import com.ghosttalk.domain.model.MessageStatus
import com.ghosttalk.ui.theme.GhostReceivedBubbleDark
import com.ghosttalk.ui.theme.GhostReceivedBubbleLight
import com.ghosttalk.ui.theme.GhostSentBubbleDark
import com.ghosttalk.ui.theme.GhostSentBubbleLight
import com.ghosttalk.ui.theme.GhostSpacing
import com.ghosttalk.ui.theme.GhostVioletLight

@Composable
fun ChatBubble(
    content: String,
    senderName: String,
    timestamp: Long,
    isSent: Boolean,
    status: MessageStatus,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val bubbleColor = when {
        isSent && isDark -> GhostSentBubbleDark
        isSent -> GhostSentBubbleLight
        isDark -> GhostReceivedBubbleDark
        else -> GhostReceivedBubbleLight
    }
    val alignment = if (isSent) Alignment.End else Alignment.Start
    val shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isSent) 16.dp else 4.dp,
        bottomEnd = if (isSent) 4.dp else 16.dp
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = GhostSpacing.xs, vertical = GhostSpacing.xxs),
        horizontalAlignment = alignment
    ) {
        Text(
            text = senderName,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = GhostSpacing.xxs, vertical = 2.dp)
        )
        Surface(
            color = bubbleColor,
            shape = shape,
            tonalElevation = if (isSent) 0.dp else 1.dp
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .padding(horizontal = GhostSpacing.sm, vertical = GhostSpacing.xs)
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(GhostSpacing.xxs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = DateTimeUtils.formatMessageTime(timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isSent) {
                        MessageStatusIcon(status = status)
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageStatusIcon(status: MessageStatus) {
    val (icon, tint, description) = when (status) {
        MessageStatus.PENDING, MessageStatus.SENDING -> Triple(
            Icons.Default.Schedule,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Sending"
        )
        MessageStatus.SENT -> Triple(
            Icons.Default.Done,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Sent"
        )
        MessageStatus.DELIVERED -> Triple(
            Icons.Default.DoneAll,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Delivered"
        )
        MessageStatus.READ -> Triple(
            Icons.Default.DoneAll,
            GhostVioletLight,
            "Read"
        )
        MessageStatus.FAILED -> Triple(
            Icons.Default.Error,
            MaterialTheme.colorScheme.error,
            "Failed to send"
        )
    }
    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = tint,
        modifier = Modifier.semantics { this.contentDescription = description }
    )
}

private fun Color.luminance(): Float {
    return 0.299f * red + 0.587f * green + 0.114f * blue
}
