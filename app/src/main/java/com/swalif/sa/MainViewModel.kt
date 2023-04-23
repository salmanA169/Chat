package com.swalif.sa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.component.Gender
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    private val userRepo = userRepository
    val isUserAvailable = userRepo.isUserAvailable()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), null
        )
    fun signOut(){
        viewModelScope.launch {
            userRepo.deleteUser(UserEntity("","","",Gender.MALE,50,""))
        }
    }
}
