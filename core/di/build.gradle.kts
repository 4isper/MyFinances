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
    implementation(project(":feature:account"))
    implementation(project(":feature:analysis"))
    implementation(project(":feature:categories"))
    implementation(project(":feature:expenses"))
    implementation(project(":feature:history"))
    implementation(project(":feature:income"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:transaction"))
    implementation(project(":feature:settings"))
}
