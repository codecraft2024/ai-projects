package com.ghosttalk.presentation.profile

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
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<GhostUser?>(null)
    val user: StateFlow<GhostUser?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { _user.value = it }
        }
    }
}
