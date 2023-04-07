package com.build

object Versions {
    const val androidx_core_version = "1.9.0"
    const val android_lifecycle_runtime = "2.5.1"
    const val activity_compose_version = "1.6.1"
    const val compose_ui_version = "1.3.3"
    const val material3_version = "1.1.0-beta02"
    const val jUnit_version = "4.13.2"
    const val text_jUnit_ext_version = "1.1.5"
    const val espersso_version = "3.5.1"
    const val hilt_compose_navigation_version = "1.0.0"
    const val hilt_version = "2.44"
    const val lifecycle_version = "2.6.1"
    const val room_version = "2.5.0"
    const val dataStore_version = "1.0.0"
    const val nav_compose_version = "2.5.3"
    const val splash_screen_version = "1.0.0"
    const val logger_square_version = "0.1"
    const val coil_version = "2.2.2"
    const val truth_version = "1.1.3"
    const val saket_swipe_version = "1.1.1"
}

object Deps {
    const val androidx_core_dep = "androidx.core:core-ktx:${Versions.androidx_core_version}"
    const val androidx_lifecycle_runtime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.android_lifecycle_runtime}"
    const val activity_compose =
        "androidx.activity:activity-compose:${Versions.activity_compose_version}"
    const val compose_ui = "androidx.compose.ui:ui:${Versions.compose_ui_version}"
    const val compose_ui_tolling_preview =
        "androidx.compose.ui:ui-tooling-preview:${Versions.compose_ui_version}"
    const val material3 = "androidx.compose.material3:material3:${Versions.material3_version}"
    const val jUnit = "junit:junit:${Versions.jUnit_version}"
    const val test_jUnit_Ext = "androidx.test.ext:junit:${Versions.text_jUnit_ext_version}"
    const val espersso = "androidx.test.espresso:espresso-core:${Versions.espersso_version}"
    const val compose_ui_test = "androidx.compose.ui:ui-test-junit4:${Versions.compose_ui_version}"
    const val compose_ui_toling = "androidx.compose.ui:ui-tooling:${Versions.compose_ui_version}"
    const val compose_ui_manfiest_test =
        "androidx.compose.ui:ui-test-manifest:${Versions.compose_ui_version}"
    const val hilt_navigation_compose =
        "androidx.hilt:hilt-navigation-compose:${Versions.hilt_compose_navigation_version}"
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt_version}"
    const val hilt_compiler_kapt =
        "com.google.dagger:hilt-android-compiler:${Versions.hilt_version}"

    const val hilt_test = "com.google.dagger:hilt-android-testing:${Versions.hilt_version}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_version}"
    const val compose_viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle_version}"
    const val lifecycle_compiler_kapt = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle_version}"
    const val room = "androidx.room:room-runtime:${Versions.room_version}"
    const val room_compiler_kapt = "androidx.room:room-compiler:${Versions.room_version}"
    const val room_ext  = "androidx.room:room-ktx:${Versions.room_version}"
    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore_version}"
    const val nav_compose = "androidx.navigation:navigation-compose:${Versions.nav_compose_version}"
    const val splash_screen = "androidx.core:core-splashscreen:${Versions.splash_screen_version}"
    const val logger_square = "com.squareup.logcat:logcat:${Versions.logger_square_version}"
    const val coil = "io.coil-kt:coil-compose:${Versions.coil_version}"
    const val truth = "com.google.truth:truth:${Versions.truth_version}"
    const val saket_swipe = "me.saket.swipe:swipe:${Versions.saket_swipe_version}"
}
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}