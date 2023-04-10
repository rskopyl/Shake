object Plugins {

    object Android {

        private const val version = "7.4.2"

        const val classpath = "com.android.tools.build:gradle:$version"
        const val id = "com.android.application"
    }

    object Kotlin {

        private const val version = "1.8.10"

        const val classpath = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val id = "org.jetbrains.kotlin.android"
    }

    object Ksp {

        private const val version = "1.8.10-1.0.9"

        const val classpath = "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$version"
        const val id = "com.google.devtools.ksp"
    }

    object Kapt {

        private const val version = "1.8.10"

        const val classpath = "org.jetbrains.kotlin.kapt:org.jetbrains.kotlin.kapt.gradle.plugin:$version"
        const val id = "org.jetbrains.kotlin.kapt"
    }

    object Hilt {

        private const val version = "2.45"

        const val classpath = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val id = "com.google.dagger.hilt.android"
    }

    object Realm {

        private const val version = "1.7.0"

        const val classpath = "io.realm.kotlin:gradle-plugin:$version"
        const val id = "io.realm.kotlin"
    }
}