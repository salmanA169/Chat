import com.build.Deps
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.github.ben-manes.versions")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}
android {
    namespace = "com.swalif.sa"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.swalif.sa"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.swalif.sa.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {

            isMinifyEnabled = true
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
            versionNameSuffix = "release"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

}

kapt {
    correctErrorTypes = true
}
dependencies {

    implementation(Deps.androidx_core_dep)
    implementation(Deps.lifecycle_runtime_compoe)
    implementation(Deps.androidx_lifecycle_runtime)
    implementation(Deps.activity_compose)
    implementation(Deps.compose_ui)
    implementation(Deps.compose_ui_tolling_preview)
    implementation(Deps.material3)
    testImplementation(Deps.jUnit)
    androidTestImplementation(Deps.test_jUnit_Ext)
    androidTestImplementation(Deps.espersso)
    androidTestImplementation(Deps.compose_ui_test)
    debugImplementation(Deps.compose_ui_toling)
    debugImplementation(Deps.compose_ui_manfiest_test)
    implementation(Deps.hilt_navigation_compose)
    implementation(Deps.hilt)
    kapt(Deps.hilt_compiler_kapt)
    implementation(Deps.viewModel)
    implementation(Deps.compose_viewModel)
    kapt(Deps.lifecycle_compiler_kapt)
    implementation(Deps.room)
    implementation(Deps.room_ext)
    kapt(Deps.room_compiler_kapt)
    implementation(Deps.dataStore)
    implementation(Deps.nav_compose)
    implementation(Deps.splash_screen)
    implementation(Deps.logger_square)
    implementation(Deps.coil)
    androidTestImplementation(Deps.hilt_test)
    androidTestImplementation(Deps.truth)
    testImplementation(Deps.truth)
    kaptAndroidTest(Deps.hilt_compiler_kapt)
    implementation(Deps.saket_swipe)
    testImplementation(Deps.coroutine_test)
    testImplementation(Deps.turbine_test)
    androidTestImplementation(Deps.turbine_test)
    implementation(platform("com.google.firebase:firebase-bom:31.5.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.5.0")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.google.firebase:firebase-storage-ktx")
    implementation("androidx.compose.runtime:runtime-livedata:1.4.3")
}