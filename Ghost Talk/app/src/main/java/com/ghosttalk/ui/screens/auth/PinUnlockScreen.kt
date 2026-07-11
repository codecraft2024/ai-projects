package com.ghosttalk.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ghosttalk.R
import com.ghosttalk.core.auth.SecureScreenEffect
import com.ghosttalk.domain.model.SavedAccount
import com.ghosttalk.ui.components.GhostTopBar
import com.ghosttalk.ui.components.auth.AccountCard
import com.ghosttalk.ui.components.auth.PinEntrySection
import com.ghosttalk.ui.theme.GhostSpacing

@Composable
fun PinUnlockScreen(
    account: SavedAccount,
    pinLength: Int,
    filledCount: Int,
    isLoading: Boolean,
    onDigit: (Int) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    SecureScreenEffect()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(GhostSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GhostTopBar(
            title = stringResource(R.string.enter_pin),
            onBackClick = onBack,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(GhostSpacing.md))
        AccountCard(
            account = account,
            onClick = {},
            showChevron = false,
            modifier = Modifier.fillMaxWidth()
        )
        PinEntrySection(
            subtitle = stringResource(R.string.enter_pin_subtitle, account.displayName),
            pinLength = pinLength,
            filledCount = filledCount,
            onDigit = onDigit,
            onDelete = onDelete,
            enabled = !isLoading,
            modifier = Modifier.weight(1f)
        )
    }
}
