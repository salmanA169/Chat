package com.swalif.sa.model

data class SignInResult(
    val userData:UserData?,
    val error:String?
)
data class UserData(
    val username:String?,
    val userId:String?,
    val photo:String?,
    val email:String?
)
