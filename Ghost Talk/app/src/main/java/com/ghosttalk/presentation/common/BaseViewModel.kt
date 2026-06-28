package com.ghosttalk.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    protected fun sendEvent(event: UiEvent) {
        viewModelScope.launch { _events.emit(event) }
    }

    protected fun setLoading() {
        _uiState.value = UiState.Loading
    }

    protected fun setError(message: String) {
        _uiState.value = UiState.Error(message)
    }

    protected fun setSuccess() {
        _uiState.value = UiState.Success
    }
}

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data object Success : UiState()
    data class Error(val message: String) : UiState()
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class Navigate(val destinationId: Int) : UiEvent()
    data object NavigateBack : UiEvent()
}
