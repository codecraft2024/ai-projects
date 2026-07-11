package com.ghosttalk.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghosttalk.R
import com.ghosttalk.presentation.splash.SplashDestination
import com.ghosttalk.presentation.splash.SplashViewModel
import com.ghosttalk.ui.components.GhostLogo
import com.ghosttalk.ui.theme.GhostSpacing
import com.ghosttalk.ui.theme.GhostTalkTheme
import com.ghosttalk.ui.theme.GhostViolet
import com.ghosttalk.ui.theme.GhostVioletDark
import com.ghosttalk.ui.theme.GhostVioletLight

private val SplashTitleGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFFFFF),
        Color(0xFFF3E8FF),
        GhostVioletLight
    )
)

private val SplashSubtitleColor = Color(0xFFD8B4FE)

@Composable
fun SplashScreen(
    onNavigate: (SplashDestination) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        destination?.let { onNavigate(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E0A3C),
                        GhostVioletDark,
                        Color(0xFF312E81)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = GhostSpacing.xl)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                GhostViolet.copy(alpha = 0.55f),
                                GhostVioletDark.copy(alpha = 0.35f)
                            )
                        ),
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(GhostSpacing.lg)
            ) {
                GhostLogo()
            }
            Spacer(modifier = Modifier.height(GhostSpacing.lg))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge.copy(
                    brush = SplashTitleGradient,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(GhostSpacing.sm))
            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = SplashSubtitleColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun SplashPreview() {
    GhostTalkTheme { SplashScreen(onNavigate = {}) }
}
