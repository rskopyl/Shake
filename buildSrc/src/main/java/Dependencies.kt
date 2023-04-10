object Dependencies {

    object Android {

        object Ktx {

            private const val version = "1.9.0"
            const val core = "androidx.core:core-ktx:$version"
        }

        object Activity {

            private const val version = "1.7.0"
            const val compose = "androidx.activity:activity-compose:$version"
        }

        object Lifecycle {

            private const val version = "2.6.1"
            const val compose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        }

        object Navigation {

            private const val version = "2.5.3"
            const val compose = "androidx.navigation:navigation-compose:$version"
        }
    }

    object Kotlin {

        object DateTime {

            private const val version = "0.4.0"
            const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:$version"
        }

        object Coroutines {

            private const val version = "1.6.4"
            const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        }
    }

    object Hilt {

        private const val version = "2.45"

        const val hilt = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-android-compiler:$version"

        object Navigation {

            private const val version = "1.0.0"
            const val compose = "androidx.hilt:hilt-navigation-compose:$version"
        }
    }

    object Interface {

        object Compose {

            private const val version = "2023.03.00"
            const val bom = "androidx.compose:compose-bom:$version"

            const val material = "androidx.compose.material3:material3"
            const val tooling = "androidx.compose.ui:ui-tooling"
            const val preview = "androidx.compose.ui:ui-tooling-preview"
        }

        object Accompanist {

            object Flow {

                private const val version = "0.30.1"
                const val flow = "com.google.accompanist:accompanist-flowlayout:$version"
            }
        }

        object Coil {

            private const val version = "2.3.0"
            const val compose = "io.coil-kt:coil-compose:$version"
        }
    }

    object Network {

        object Retrofit {

            private const val version = "2.9.0"

            const val retrofit = "com.squareup.retrofit2:retrofit:$version"
            const val converter = "com.squareup.retrofit2:converter-moshi:$version"
        }

        object Moshi {

            private const val version = "1.14.0"

            const val moshi = "com.squareup.moshi:moshi-kotlin:$version"
            const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
        }
    }

    object Storage {

        object Realm {

            private const val version = "1.7.0"
            const val base = "io.realm.kotlin:library-base:$version"
        }
    }
}