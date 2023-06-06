package com.swalif.sa.features.onboarding.registration.registration

import android.content.IntentSender
import com.swalif.sa.model.UserData

data class RegistrationState(
    val showGoogleSignInt : IntentSender? = null,
    val email:String = "",
    val password:String = "",
    val showEmailError:Boolean = false,
    val showPasswordError:Boolean = false
)
