package com.swalif.sa.features.onboarding.registration.registration

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.features.onboarding.signup.SignUpEvent
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

    private val _event = MutableStateFlow<RegistrationEvent?>(null)
    val event = _event.asStateFlow()

    init {
        if (userRepository is UserRepositoryImpl) {
            addCloseable(userRepository)
        }
    }

    private suspend fun insertUser(uidUser: String) {
        val getUser = userRepository.getUserByUid(uidUser)
        if (getUser != null) {
            userRepository.insertUser(getUser)
        }
    }

    fun signIn() {
        viewModelScope.launch(dispatcherProvider.io) {
            val state = _currentRegistrationState.value
            if (checkEmailAndPassword(state.email,state.password)){
                val signinResult = userRepository.signIn(state.email,state.password)
                val userData = signinResult.userData
                if (userData!= null){
                    insertUser(userData.userId!!)
                    _event.update {
                        RegistrationEvent.NavigateHomeScreen(userData.username!!)
                    }
                }else {
                    val messageError = signinResult.error
                    if (messageError!= null ){
                        _event.update {
                           RegistrationEvent.ErrorMessage(messageError)
                        }
                    }
                }
            }
        }
    }

    private fun checkEmailAndPassword(email:String,password: String):Boolean{
        _currentRegistrationState.update {
            it.copy(
                showEmailError = email.isEmpty(),
                showPasswordError = password.isEmpty()
            )
        }
        return !(email.isEmpty() || password.isEmpty())
    }
    fun updateEmail(email: String) {
        _currentRegistrationState.update {
            it.copy(
                email = email
            )
        }
    }

    fun updatePassword(password: String) {
        _currentRegistrationState.update {
            it.copy(
                password = password
            )
        }
    }

    fun signInResult(intent: Intent) {
        viewModelScope.launch(dispatcherProvider.default) {
            val getResult = userRepository.getSignInResult(intent)
            logcat {
                getResult.toString()
            }
            if (getResult.userData != null) {
                val userData = getResult.userData
                if (!getResult.userData.isNewUser) {
                    insertUser(getResult.userData.userId!!)
                    _event.update {
                        RegistrationEvent.NavigateHomeScreen(userData.username!!)
                    }
                } else {
                    _event.update {
                        RegistrationEvent.NavigateInfo(
                            userData.email!!,
                            userData.photo!!,
                            userData.userId!!,
                            userData.username!!
                        )
                    }
                }

            } else {
                if (getResult.error != null) {
                    _event.update {
                        RegistrationEvent.ErrorMessage(getResult.error)
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

sealed class RegistrationEvent {
    class NavigateHomeScreen(val userName:String) : RegistrationEvent()
    class NavigateInfo(
        val email: String,
        val photo: String,
        val uidUser: String,
        val username: String
    ) : RegistrationEvent()

    class ErrorMessage(val message: String) : RegistrationEvent()
}