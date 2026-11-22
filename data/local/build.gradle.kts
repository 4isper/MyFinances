plugins {
    id("myfinances.android.library")
    id("myfinances.room")
}

android {
    namespace = "com.m4isper.local"
}

dependencies {
    implementation(libs.androidx.sqlite.bundled)
    // DAO-тесты — instrumented: android-вариант BundledSQLiteDriver тянет
    // android.database.sqlite, которого нет в JVM-заглушках unit-тестов
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
