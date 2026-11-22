plugins {
    id("myfinances.kotlin.library")
}

// Тестовые фейки репозиториев общие для всех feature-модулей;
// в kover-агрегацию и detekt-отчёты модуль осознанно не включён как продакшн-код.
dependencies {
    api(project(":domain"))
    // domain прячет core:common за implementation — для Result в фейках нужен прямой доступ
    api(project(":core:common"))
    api(libs.kotlinx.coroutines.core)
    // MainDispatcherRule живёт в main-исходниках, поэтому junit/test — не только в testImplementation
    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
}
