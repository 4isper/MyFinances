plugins {
    id("myfinances.android.library")
    id("myfinances.compose")
    id("myfinances.dagger")
}

android {
    namespace = "com.m4isper.transaction"
}

dependencies {
    testImplementation(project(":core:testing"))
    implementation(project(":domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:resources"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
