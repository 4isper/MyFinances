plugins {
    id("myfinances.android.library")
    id("myfinances.dagger")
}

android {
    namespace = "com.m4isper.di"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data:repository"))
    implementation(project(":data:remote"))
    implementation(project(":feature:account"))
    implementation(project(":feature:analysis"))
    implementation(project(":feature:categories"))
    implementation(project(":feature:expenses"))
    implementation(project(":feature:history"))
    implementation(project(":feature:income"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:transaction"))
    implementation(project(":feature:settings"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
}
