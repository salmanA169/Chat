package com.swalif.sa.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class CustomRipple(private val rippleColor :Color):RippleTheme {

    @Composable
    override fun defaultColor(): Color {
       return  RippleTheme.defaultRippleColor(rippleColor, isSystemInDarkTheme())
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleTheme.defaultRippleAlpha(rippleColor, isSystemInDarkTheme())
    }
}