plugins {
    `kotlin-dsl`
}

group = "com.m4isper.buildlogic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.compose.compiler.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "myfinances.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = "myfinances.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("compose") {
            id = "myfinances.compose"
            implementationClass = "ComposeConventionPlugin"
        }
        register("dagger") {
            id = "myfinances.dagger"
            implementationClass = "DaggerConventionPlugin"
        }
        register("room") {
            id = "myfinances.room"
            implementationClass = "RoomConventionPlugin"
        }
    }
}
