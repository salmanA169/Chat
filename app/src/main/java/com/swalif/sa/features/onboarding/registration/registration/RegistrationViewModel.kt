package com.swalif.sa.features.onboarding.registration.registration

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.model.UserInfo
import com.swalif.sa.repository.userRepository.UserRepository
import com.swalif.sa.repository.userRepository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _currentRegistrationState = MutableStateFlow(RegistrationState())
    val currentRegistrationState = _currentRegistrationState.asStateFlow()

    init {
        if (userRepository is UserRepositoryImpl) {
            addCloseable(userRepository)
        }
    }

    private suspend fun insertUser(uidUser:String){
        val getUser = userRepository.getUserByUid(uidUser)
        if (getUser != null){
            userRepository.insertUser(getUser)
        }
    }

    fun signInResult(intent: Intent) {
        viewModelScope.launch(dispatcherProvider.default) {
            val getResult = userRepository.getSignInResult(intent)
            logcat {
                getResult.toString()
            }
            if (getResult.userData != null) {
                if (!getResult.userData.isNewUser){
                    insertUser(getResult.userData.userId!!)
                }
                _currentRegistrationState.update {
                    it.copy(
                        userData = getResult.userData
                    )
                }
            } else {
                if (getResult.error != null) {
                    _currentRegistrationState.update {
                        it.copy(
                            messageError = getResult.error
                        )
                    }
                }
            }

        }
    }

    fun signInGoogleOneTap() {
        viewModelScope.launch(dispatcherProvider.default) {
            val intent = userRepository.signIn()
            _currentRegistrationState.update {
                it.copy(
                    intent
                )
            }
        }
    }
}