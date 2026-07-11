package com.ghosttalk.ui.components.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghosttalk.core.avatar.ModernAvatarCatalog
import com.ghosttalk.ui.theme.GhostSpacing

@Composable
fun AvatarPickerGrid(
    selectedId: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 4
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(GhostSpacing.sm)
    ) {
        ModernAvatarCatalog.all.chunked(columns).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(GhostSpacing.sm, Alignment.CenterHorizontally)
            ) {
                row.forEach { avatar ->
                    val selected = avatar.id == selectedId
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(avatar.background)
                            .border(
                                width = if (selected) 3.dp else 0.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .clickable { onSelected(avatar.id) }
                            .semantics { contentDescription = avatar.label },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = avatar.emoji, fontSize = 32.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ModernAvatarView(
    avatarId: String,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 48.dp
) {
    val avatar = ModernAvatarCatalog.find(avatarId) ?: ModernAvatarCatalog.all.first()
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(avatar.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = avatar.emoji,
            fontSize = (size.value * 0.45f).sp,
            textAlign = TextAlign.Center
        )
    }
}
