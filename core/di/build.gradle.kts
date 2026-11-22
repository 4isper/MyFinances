plugins {
    id("myfinances.android.library")
    id("myfinances.dagger")
}

android {
    namespace = "com.m4isper.di"
}

dependencies {
    implementation(project(":data:local"))
    implementation(libs.androidx.room3.runtime)
    implementation(libs.androidx.sqlite.bundled)
    implementation(project(":domain"))
    implementation(project(":data:repository"))
    implementation(project(":data:preferences"))
}
