import com.build.Deps
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.github.ben-manes.versions")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
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
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        kotlinCompilerExtensionVersion = "1.4.2"
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
    implementation(Deps.androidx_lifecycle_runtime)
    implementation(Deps.activity_compose)
    implementation(Deps.compose_ui)
    implementation(Deps.compose_ui_tolling_preview)
    implementation(Deps.material3)
    testImplementation(Deps.jUnit)
    testImplementation("org.junit.jupiter:junit-jupiter")
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
}