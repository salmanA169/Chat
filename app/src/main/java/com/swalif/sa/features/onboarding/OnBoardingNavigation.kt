package com.swalif.sa.features.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.swalif.sa.ON_BOARDING_SCREEN
import com.swalif.sa.Screens
import com.swalif.sa.features.onboarding.registration.information.informationDest
import com.swalif.sa.features.onboarding.registration.registration.registrationDest

fun NavGraphBuilder.onBoardingNavigation(navController: NavController) {
    navigation(
        Screens.OnBoardingScreen.RegistrationScreen.route,
        ON_BOARDING_SCREEN
    ) {
        registrationDest(navController)
        informationDest(navController)
    }
}