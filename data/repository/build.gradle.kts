plugins {
    id("myfinances.android.library")
}

android {
    namespace = "com.m4isper.repository"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":data:preferences"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
}
