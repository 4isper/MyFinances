plugins {
    id("myfinances.android.library")
}

android {
    namespace = "com.m4isper.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
