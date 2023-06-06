package com.swalif.sa.features.onboarding.signup

data class SignupState(
    val image:String = "",
    val username:String = "",
    val email:String = "",
    val password:String = "",
    val showProgress:Boolean = false,
    val showUserNameError:Boolean = false,
    val showEmailError:Boolean = false,
    val showPasswordError:Boolean = false
)
