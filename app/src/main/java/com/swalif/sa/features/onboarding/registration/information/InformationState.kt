package com.swalif.sa.features.onboarding.registration.information

import androidx.compose.runtime.Immutable
import com.swalif.sa.component.Gender
import com.swalif.sa.utils.isEmailValid

@Immutable
data class InformationState(
    val name:String = "",
    val email:String = "",
    val gender:Gender? = null,
    val imageUri:String = "",
    val showEmailError:Boolean = false,
    val showNameError:Boolean = false,
    val showGenderError:Boolean = false,
    val isLoading :Boolean = false
){
    fun checkIfHasError ():Boolean = name.isEmpty() || !email.isEmailValid || gender == null
}