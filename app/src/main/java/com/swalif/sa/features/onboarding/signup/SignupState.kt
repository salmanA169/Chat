package com.swalif.sa.features.onboarding.signup

data class SignupState(
    val image:String = "",
    val userName:String = "",
    val email:String = "",
    val password:String = "",
    val showProgress:Boolean = false
)
