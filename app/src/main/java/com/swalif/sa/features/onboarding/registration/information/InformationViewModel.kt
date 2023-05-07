package com.swalif.sa.features.onboarding.registration.information

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.EMAIL_ARG
import com.swalif.sa.MY_UID_ARG
import com.swalif.sa.PHOTO_ARG
import com.swalif.sa.USERNAME_ARG
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

// TODO: save data to room and firestore and data store and manage them later
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

        }
    }
    private fun onEvent(infoEvent: InfoEvent){
        _infoEvent.update {
            infoEvent
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
            _infoState.update {
                it.copy(
                    isLoading = true
                )
            }
            userRepository.insertUser(userEntity)
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
    private fun updateGender(gender:Gender){
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