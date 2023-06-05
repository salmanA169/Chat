package com.swalif.sa.features.onboarding.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor():ViewModel() {

    private val _state = MutableStateFlow(SignupState())
     val state = _state.asStateFlow()

    private val _event = MutableStateFlow<SignUpEvent?>(null)
    val event = _event.asStateFlow()

    fun signUp(){

    }

    fun updateImage(image:String){
        _state.update {
            it.copy(
                image = image
            )
        }
    }

}

sealed class SignUpEvent{
    class Navigate(val name:String,val email:String,val image:String,val uid:String):SignUpEvent()
}