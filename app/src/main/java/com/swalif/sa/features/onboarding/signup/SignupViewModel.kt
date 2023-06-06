package com.swalif.sa.features.onboarding.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.repository.userRepository.UserRepository
import com.swalif.sa.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

    private val _event = MutableStateFlow<SignUpEvent?>(null)
    val event = _event.asStateFlow()

    fun signUp() {
        if (checkError()) {
            viewModelScope.launch(dispatcherProvider.io) {
                val stateSignup = _state.value
                val signupResult = userRepository.signUpWithEmailAndPassword(
                    stateSignup.username,
                    stateSignup.email,
                    stateSignup.password,
                    stateSignup.image
                )
                if (signupResult.error!= null){
                    _event.update {
                        SignUpEvent.MessageError(signupResult.error)
                    }
                }else{
                    val userData = signupResult.userData!!
                    _event.update {
                        SignUpEvent.Navigate(userData.username!!,userData.email!!,userData.photo!!,userData.userId!!)
                    }
                }
            }
        }
    }

    fun updateImage(image: String) {
        _state.update {
            it.copy(
                image = image
            )
        }
    }

    fun updateUserName(username: String) {
        _state.update {
            it.copy(
                username = username
            )
        }
    }

    fun updateEmail(email: String) {
        _state.update {
            it.copy(
                email = email
            )
        }
    }

    fun updatePassword(password: String) {
        _state.update {
            it.copy(
                password = password
            )
        }
    }

    private fun checkError(): Boolean {
        val signupState = _state.value

        if (signupState.image.isEmpty()) {
            _event.update {
                SignUpEvent.MessageError("You must pick image")
            }
            _state.update {
                it.copy(
                    showEmailError = !it.email.isEmailValid,
                    showPasswordError = it.password.isEmpty(),
                    showUserNameError = it.username.isEmpty()
                )
            }
            return false
        }
        _state.update {
            it.copy(
                showEmailError = !it.email.isEmailValid,
                showPasswordError = it.password.isEmpty(),
                showUserNameError = it.username.isEmpty()
            )
        }
        return !(!signupState.email.isEmailValid || signupState.username.isEmpty() || signupState.password.isEmpty())
    }

}

sealed class SignUpEvent {
    class Navigate(val name: String, val email: String, val image: String, val uid: String) :
        SignUpEvent()

    class MessageError(val message: String) : SignUpEvent()
}