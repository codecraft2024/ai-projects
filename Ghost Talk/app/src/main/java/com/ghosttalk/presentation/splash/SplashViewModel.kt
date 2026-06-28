package com.ghosttalk.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.core.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SplashDestination {
    ONBOARDING,
    LOGIN,
    HOME
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            combine(
                sessionManager.isLoggedIn,
                sessionManager.hasCompletedOnboarding
            ) { loggedIn, onboardingDone ->
                when {
                    loggedIn -> SplashDestination.HOME
                    onboardingDone -> SplashDestination.LOGIN
                    else -> SplashDestination.ONBOARDING
                }
            }.collect { dest ->
                _destination.value = dest
            }
        }
    }
}
