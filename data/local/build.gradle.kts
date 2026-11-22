plugins {
    id("myfinances.android.library")
    id("myfinances.room")
}

android {
    namespace = "com.m4isper.local"
}

dependencies {
    implementation(libs.androidx.sqlite.bundled)
}
