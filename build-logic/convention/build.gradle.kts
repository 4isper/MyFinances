plugins {
    `kotlin-dsl`
}

group = "com.m4isper.buildlogic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.compiler.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.ktlint.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.kover.gradlePlugin)
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
        register("kotlinLibrary") {
            id = "myfinances.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("ktlint") {
            id = "myfinances.ktlint"
            implementationClass = "KtlintConventionPlugin"
        }
        register("detekt") {
            id = "myfinances.detekt"
            implementationClass = "DetektConventionPlugin"
        }
        register("kover") {
            id = "myfinances.kover"
            implementationClass = "KoverConventionPlugin"
        }
    }
}
