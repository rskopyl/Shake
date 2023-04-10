@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id(Plugins.Android.id)
    id(Plugins.Kotlin.id)
    id(Plugins.Ksp.id)
    id(Plugins.Kapt.id)
    id(Plugins.Hilt.id)
    id(Plugins.Realm.id)
}

android {
    defaultConfig {
        applicationId = Config.applicationId
        namespace = Config.namespace

        versionCode = Config.versionCode
        versionName = Config.versionName

        targetSdk = Config.targetSdk
        compileSdk = Config.compileSdk
        minSdk = Config.minSdk
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Config.compilerExtensionVersion
    }
}

dependencies {
    // Android
    implementation(Dependencies.Android.Ktx.core)
    implementation(Dependencies.Android.Activity.compose)
    implementation(Dependencies.Android.Lifecycle.compose)
    implementation(Dependencies.Android.Navigation.compose)

    // Kotlin
    implementation(Dependencies.Kotlin.DateTime.dateTime)
    implementation(Dependencies.Kotlin.Coroutines.coroutines)

    // Hilt
    implementation(Dependencies.Hilt.hilt)
    implementation(Dependencies.Hilt.Navigation.compose)
    kapt(Dependencies.Hilt.compiler)

    // Interface
    val composeBom = platform(Dependencies.Interface.Compose.bom)
    implementation(composeBom)
    implementation(Dependencies.Interface.Compose.material)
    implementation(Dependencies.Interface.Compose.tooling)
    implementation(Dependencies.Interface.Compose.preview)
    implementation(Dependencies.Interface.Accompanist.Flow.flow)
    implementation(Dependencies.Interface.Coil.compose)

    // Network
    implementation(Dependencies.Network.Retrofit.retrofit)
    implementation(Dependencies.Network.Retrofit.converter)
    implementation(Dependencies.Network.Moshi.moshi)
    ksp(Dependencies.Network.Moshi.codegen)

    // Storage
    implementation(Dependencies.Storage.Realm.base)
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions.freeCompilerArgs.addAll(
        "-opt-in=kotlinx.coroutines.FlowPreview",
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    )
}