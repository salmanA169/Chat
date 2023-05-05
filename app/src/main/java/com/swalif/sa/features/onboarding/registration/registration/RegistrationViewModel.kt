package com.swalif.sa.features.onboarding.registration.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.repository.userRepository.UserRepository
import com.swalif.sa.repository.userRepository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _currentRegistrationState = MutableStateFlow(RegistrationState())
    val currentRegistrationState =
        _currentRegistrationState.combine(userRepository.authState()) { registrationState, cureentAuthstate ->
            registrationState.copy(
                cureentAuthstate
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), RegistrationState()
        )

    init {
        if (userRepository is UserRepositoryImpl){
            addCloseable(userRepository)
        }
    }
    fun signInGoogleOneTap() {
        viewModelScope.launch(dispatcherProvider.default) {
            userRepository.signIn()
        }
    }
}