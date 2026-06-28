package com.ghosttalk.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghosttalk.domain.model.GhostUser
import com.ghosttalk.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow<GhostUser?>(null)
    val currentUser: StateFlow<GhostUser?> = _currentUser.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { _currentUser.value = it }
        }
    }
}
