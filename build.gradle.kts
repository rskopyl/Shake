buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Plugins.Android.classpath)
        classpath(Plugins.Kotlin.classpath)
        classpath(Plugins.Ksp.classpath)
        classpath(Plugins.Kapt.classpath)
        classpath(Plugins.Hilt.classpath)
        classpath(Plugins.Realm.classpath)
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}