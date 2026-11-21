plugins {
    id("myfinances.android.library")
}

android {
    namespace = "com.m4isper.preferences"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.datastore.preferences)
}
