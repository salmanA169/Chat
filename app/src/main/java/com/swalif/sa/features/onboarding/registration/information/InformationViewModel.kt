package com.swalif.sa.features.onboarding.registration.information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.component.Gender
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.repository.userRepository.UserRepository
import com.swalif.sa.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _infoState = MutableStateFlow(InformationState())
    val infoState = _infoState.asStateFlow()

    private val _infoEvent = MutableStateFlow<InfoEvent?>(null)
    val infoEvent = _infoEvent.asStateFlow()

    fun checkAndSave(gender: Gender){
        updateGender(gender)
        val info = _infoState.value
        if (info.checkIfHasError()){
            checkError()
        }else{
            insertUser(
                UserEntity(
                    "test",info.name,info.email,gender,"",500,""
                )
            )
            _infoEvent.update {
                InfoEvent.Saved
            }
        }
    }
    private fun checkError() {
        _infoState.update {
            it.copy(
                showEmailError = !it.email.isEmailValid,
                showNameError = it.name.isEmpty(),
                showGenderError = it.gender == null
            )
        }
    }
    private fun insertUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(userEntity)
        }
    }

    fun updateUsername(userName: String) {
        _infoState.update {
            it.copy(
                name = userName
            )
        }
    }
    fun updateGender(gender:Gender){
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

sealed class InfoEvent{
    object Saved:InfoEvent()
}