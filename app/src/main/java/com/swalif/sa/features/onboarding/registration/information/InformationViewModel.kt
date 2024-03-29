package com.swalif.sa.features.onboarding.registration.information

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.swalif.sa.EMAIL_ARG
import com.swalif.sa.MY_UID_ARG
import com.swalif.sa.PHOTO_ARG
import com.swalif.sa.USERNAME_ARG
import com.swalif.sa.component.Gender
import com.swalif.sa.model.UserInfo
import com.swalif.sa.repository.userRepository.UserRepository
import com.swalif.sa.utils.generateUniqueId
import com.swalif.sa.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val emailArg = savedStateHandle.get<String>(EMAIL_ARG)
    private val userNameArg = savedStateHandle.get<String>(USERNAME_ARG)
    private val photoArg = savedStateHandle.get<String>(PHOTO_ARG)
    private val myUidArg = savedStateHandle.get<String>(MY_UID_ARG)
    private val _infoState = MutableStateFlow(InformationState())
    val infoState = _infoState.asStateFlow()

    init {
        _infoState.update {
            it.copy(
                email = emailArg!!,
                name = userNameArg!!,
                imageUri = photoArg!!
            )
        }
    }

    private val _infoEvent = MutableStateFlow<InfoEvent?>(null)
    val infoEvent = _infoEvent.asStateFlow()

    fun checkAndSave(gender: Gender) {
        updateGender(gender)
        val info = _infoState.value
        if (info.checkIfHasError()) {
            checkError()
        } else {
            val user = _infoState.value
            insertUser(
                UserInfo(
                    myUidArg!!,
                    user.name,
                    user.email,
                    gender,
                    generateUniqueId(),
                    Timestamp.now().toDate().time,
                    user.imageUri
                )
            )

        }
    }

    private fun onEvent(infoEvent: InfoEvent) {
        _infoEvent.update {
            infoEvent
        }
    }

    private fun checkError() {
        _infoState.update {
            it.copy(
                showEmailError = !it.email.isEmailValid,
                showNameError = it.name.isEmpty(),
                showGenderError = it.gender == null,
                isLoading = false
            )
        }
    }

    private fun insertUser(userEntity: UserInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            _infoState.update {
                it.copy(
                    isLoading = true
                )
            }
            userRepository.saveUser(userEntity)
            _infoState.update {
                it.copy(
                    isLoading = false
                )
            }
            onEvent(InfoEvent.Saved)

        }
    }

    fun updateUsername(userName: String) {
        _infoState.update {
            it.copy(
                name = userName
            )
        }
    }

    private fun updateGender(gender: Gender) {
        _infoState.update {
            it.copy(
                gender = gender
            )
        }
    }

    fun updateEmail(email: String) {
        _infoState.update {
            it.copy(
                email = email
            )
        }
    }
}

sealed class InfoEvent {

    object Saved : InfoEvent()
}