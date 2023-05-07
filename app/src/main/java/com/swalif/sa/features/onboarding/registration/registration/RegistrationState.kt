package com.swalif.sa.features.onboarding.registration.registration

import android.content.IntentSender
import com.swalif.sa.model.UserData

data class RegistrationState(
    val showGoogleSignInt : IntentSender? = null,
    val userData: UserData? = null,
    val messageError:String?= null
)
