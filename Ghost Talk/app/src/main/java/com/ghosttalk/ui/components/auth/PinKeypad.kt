package com.ghosttalk.ui.components.auth

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghosttalk.ui.theme.GhostSpacing

@Composable
fun PinIndicators(
    pinLength: Int,
    filledCount: Int,
    modifier: Modifier = Modifier,
    pinSize: Int = 6
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pinSize) { index ->
            val filled = index < filledCount
            val color by animateColorAsState(
                targetValue = if (filled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                label = "pin_dot"
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = GhostSpacing.xs)
                    .size(if (filled) 16.dp else 14.dp)
                    .background(color, CircleShape)
                    .semantics {
                        contentDescription = if (filled) "PIN digit entered" else "PIN digit empty"
                    }
            )
        }
    }
}

@Composable
fun PinKeypad(
    onDigit: (Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "del")
    )
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(GhostSpacing.sm)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    when (key) {
                        "" -> Box(modifier = Modifier.size(72.dp))
                        "del" -> PinKey(
                            label = null,
                            icon = Icons.AutoMirrored.Filled.Backspace,
                            onClick = onDelete,
                            enabled = enabled,
                            contentDescription = "Delete"
                        )
                        else -> PinKey(
                            label = key,
                            onClick = { onDigit(key.toInt()) },
                            enabled = enabled,
                            contentDescription = "Digit $key"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PinKey(
    label: String?,
    onClick: () -> Unit,
    enabled: Boolean,
    contentDescription: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "key_scale"
    )
    Box(
        modifier = Modifier
            .size(72.dp)
            .scale(scale)
            .background(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (enabled) 0.65f else 0.3f),
                CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled
            ) {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onClick()
            }
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
        } else {
            Text(
                text = label.orEmpty(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
