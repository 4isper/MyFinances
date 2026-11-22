plugins {
    id("myfinances.android.library")
    id("myfinances.compose")
}

android {
    namespace = "com.m4isper.ui"
}

dependencies {
    api(libs.androidx.material.icons.core)
    implementation(project(":core:resources"))
    implementation(project(":core:common"))
}
