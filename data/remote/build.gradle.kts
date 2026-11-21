plugins {
    id("myfinances.android.library")
}

android {
    namespace = "com.m4isper.remote"
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
}
