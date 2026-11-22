plugins {
    id("myfinances.android.library")
}

android {
    namespace = "com.m4isper.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
