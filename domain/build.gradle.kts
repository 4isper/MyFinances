plugins {
    id("myfinances.kotlin.library")
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
