package com.ghosttalk.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ghosttalk.R
import com.ghosttalk.domain.model.Chat
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.presentation.discover.DiscoveryViewModel
import com.ghosttalk.ui.components.GhostSearchBar
import com.ghosttalk.ui.components.GhostAvatar
import com.ghosttalk.ui.theme.GhostOnline
import com.ghosttalk.ui.theme.GhostSpacing

private val discoverQuickSearches = listOf("ghost", "shadow", "anon")

@Composable
fun DiscoveryScreen(
    onUserOpen: (GhostUser) -> Unit,
    onChatOpen: (Chat) -> Unit,
    viewModel: DiscoveryViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val chatCreated by viewModel.chatCreated.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let { snackbar.showSnackbar(it); viewModel.clearError() }
    }
    LaunchedEffect(chatCreated) {
        chatCreated?.let {
            onChatOpen(it)
            viewModel.clearChatCreated()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = GhostSpacing.md),
            verticalArrangement = Arrangement.spacedBy(GhostSpacing.sm)
        ) {
            item(key = "header") {
                DiscoverHeader(
                    query = query,
                    onQueryChange = viewModel::onQueryChange,
                    resultCount = users.size,
                    isLoading = isLoading,
                    onQuickSearch = viewModel::onQueryChange
                )
            }

            when {
                isLoading && users.isEmpty() -> {
                    items(5, key = { "skeleton_$it" }) {
                        UserCardSkeleton()
                    }
                }
                users.isEmpty() -> {
                    item(key = "empty") {
                        DiscoverEmptyPanel(
                            query = query,
                            onSuggestionClick = viewModel::onQueryChange
                        )
                    }
                }
                else -> {
                    items(users, key = { it.ghostId }) { user ->
                        UserDirectoryCard(
                            user = user,
                            onClick = { onUserOpen(user) },
                            onMessage = { viewModel.startChat(user.ghostId) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DiscoverHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    resultCount: Int,
    isLoading: Boolean,
    onQuickSearch: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = GhostSpacing.md, vertical = GhostSpacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.discover_directory),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.discover_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (resultCount > 0) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = stringResource(R.string.discover_results_count, resultCount),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        GhostSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            placeholder = stringResource(R.string.search_users_hint),
            modifier = Modifier.fillMaxWidth()
        )
        if (query.isBlank()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(GhostSpacing.xs),
                verticalArrangement = Arrangement.spacedBy(GhostSpacing.xs)
            ) {
                discoverQuickSearches.forEach { term ->
                    FilterChip(
                        selected = false,
                        onClick = { onQuickSearch(term) },
                        label = { Text(term) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Explore,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoverEmptyPanel(
    query: String,
    onSuggestionClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = GhostSpacing.md),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
    ) {
        Column(
            modifier = Modifier.padding(GhostSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.PersonSearch,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(GhostSpacing.sm))
            Text(
                text = if (query.isNotBlank()) {
                    stringResource(R.string.no_users_found)
                } else {
                    stringResource(R.string.discover_empty)
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(Modifier.height(GhostSpacing.xs))
            Text(
                text = stringResource(R.string.discover_try_search),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            if (query.isBlank()) {
                Spacer(Modifier.height(GhostSpacing.md))
                Row(horizontalArrangement = Arrangement.spacedBy(GhostSpacing.xs)) {
                    discoverQuickSearches.take(3).forEach { term ->
                        FilterChip(
                            selected = false,
                            onClick = { onSuggestionClick(term) },
                            label = { Text(term) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserCardSkeleton() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = GhostSpacing.md),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(GhostSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(Modifier.width(GhostSpacing.sm))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                )
            }
        }
    }
}

@Composable
fun UserDirectoryCard(
    user: GhostUser,
    onClick: () -> Unit,
    onMessage: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = GhostSpacing.md),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GhostSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GhostAvatar(
                avatarId = user.avatarResId,
                size = 52.dp,
                isOnline = user.isOnline,
                contentDescription = user.displayName
            )
            Spacer(Modifier.width(GhostSpacing.sm))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = user.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (user.verified) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(16.dp)
                        )
                    }
                }
                Text(
                    text = "@${user.nickname}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(
                                if (user.isOnline) GhostOnline
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (user.isOnline) stringResource(R.string.online)
                        else stringResource(R.string.offline),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (user.isOnline) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    user.bio?.takeIf { it.isNotBlank() }?.let { bio ->
                        Text(
                            text = " · $bio",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                }
            }
            FilledTonalIconButton(
                onClick = onMessage,
                modifier = Modifier.padding(start = GhostSpacing.xs)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.message)
                )
            }
        }
    }
}
