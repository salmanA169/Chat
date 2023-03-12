package com.swalif.sa.utils

import android.util.Patterns


val CharSequence.isEmailValid: Boolean
    get() = Patterns.EMAIL_ADDRESS.matcher(this).matches()
