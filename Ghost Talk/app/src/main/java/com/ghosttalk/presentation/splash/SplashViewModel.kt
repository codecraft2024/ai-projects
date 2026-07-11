package com.ghosttalk.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.core.auth.AccountManager
import com.ghosttalk.core.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SplashDestination {
    ONBOARDING,
    ACCOUNT_HUB,
    HOME
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val accountManager: AccountManager
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1500)
            val loggedIn = sessionManager.isLoggedIn.first()
            if (loggedIn) {
                _destination.value = SplashDestination.HOME
                return@launch
            }
            combine(
                sessionManager.hasCompletedOnboarding,
                accountManager.accounts
            ) { onboardingDone, accounts ->
                when {
                    !onboardingDone -> SplashDestination.ONBOARDING
                    else -> SplashDestination.ACCOUNT_HUB
                }
            }.collect { dest ->
                _destination.value = dest
            }
        }
    }
}
