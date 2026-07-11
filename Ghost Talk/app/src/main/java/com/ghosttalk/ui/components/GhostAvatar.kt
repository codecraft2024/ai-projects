package com.ghosttalk.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ghosttalk.core.utils.AvatarProvider
import com.ghosttalk.ui.components.auth.ModernAvatarView
import com.ghosttalk.ui.theme.GhostOnline
import com.ghosttalk.ui.theme.GhostSpacing
import com.ghosttalk.ui.theme.GhostViolet

@Composable
fun GhostLogo(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    contentDescription: String = "Ghost Talk logo"
) {
    Canvas(
        modifier = modifier
            .size(size)
            .semantics { this.contentDescription = contentDescription }
    ) {
        val w = this.size.width
        val h = this.size.height
        val body = Path().apply {
            moveTo(w * 0.5f, h * 0.18f)
            cubicTo(w * 0.28f, h * 0.18f, w * 0.18f, h * 0.38f, w * 0.18f, h * 0.52f)
            cubicTo(w * 0.18f, h * 0.62f, w * 0.22f, h * 0.7f, w * 0.26f, h * 0.76f)
            lineTo(w * 0.26f, h * 0.88f)
            lineTo(w * 0.34f, h * 0.84f)
            lineTo(w * 0.38f, h * 0.9f)
            lineTo(w * 0.42f, h * 0.84f)
            lineTo(w * 0.46f, h * 0.9f)
            lineTo(w * 0.5f, h * 0.84f)
            lineTo(w * 0.54f, h * 0.9f)
            lineTo(w * 0.58f, h * 0.84f)
            lineTo(w * 0.62f, h * 0.9f)
            lineTo(w * 0.66f, h * 0.84f)
            lineTo(w * 0.74f, h * 0.88f)
            lineTo(w * 0.74f, h * 0.76f)
            cubicTo(w * 0.78f, h * 0.7f, w * 0.82f, h * 0.62f, w * 0.82f, h * 0.52f)
            cubicTo(w * 0.82f, h * 0.38f, w * 0.72f, h * 0.18f, w * 0.5f, h * 0.18f)
            close()
        }
        drawPath(body, Color.White, style = Fill)
        drawCircle(GhostViolet, radius = w * 0.05f, center = center.copy(x = w * 0.38f, y = h * 0.48f))
        drawCircle(GhostViolet, radius = w * 0.05f, center = center.copy(x = w * 0.62f, y = h * 0.48f))
        drawPath(
            Path().apply {
                moveTo(w * 0.4f, h * 0.6f)
                quadraticBezierTo(w * 0.5f, h * 0.66f, w * 0.6f, h * 0.6f)
            },
            GhostViolet,
            style = Stroke(width = w * 0.02f)
        )
    }
}

@Composable
fun GhostAvatar(
    avatarId: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    isOnline: Boolean = false,
    contentDescription: String? = null
) {
    Box(modifier = modifier.size(size)) {
        if (avatarId.startsWith("modern_")) {
            ModernAvatarView(
                avatarId = avatarId,
                modifier = Modifier.align(Alignment.Center),
                size = size
            )
        } else {
            Image(
                painter = painterResource(AvatarProvider.getDrawableRes(avatarId)),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(size)
                    .align(Alignment.Center)
            )
        }
        if (isOnline) {
            OnlineIndicator(
                modifier = Modifier.align(Alignment.BottomEnd),
                size = (size.value * 0.25f).dp
            )
        }
    }
}

@Composable
fun OnlineIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 12.dp
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    Canvas(modifier = modifier.size(size)) {
        drawCircle(
            color = surfaceColor,
            radius = size.toPx() / 2f
        )
        drawCircle(
            color = GhostOnline,
            radius = size.toPx() * 0.38f
        )
    }
}

@Composable
fun UnreadBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    if (count <= 0) return
    androidx.compose.material3.Badge(
        modifier = modifier.semantics {
            contentDescription = "$count unread messages"
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        androidx.compose.material3.Text(
            text = if (count > 99) "99+" else count.toString(),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.semantics(mergeDescendants = true) {}
        )
    }
}
