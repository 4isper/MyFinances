plugins {
    id("myfinances.android.library")
    id("myfinances.dagger")
}

android {
    namespace = "com.m4isper.di"
}

// composition root: компоненты Dagger знают фабрики ViewModel всех фич —
// это их законная работа, а не мёртвые зависимости
dependencies {
    implementation(project(":data:local"))
    implementation(libs.androidx.room3.runtime)
    implementation(libs.androidx.sqlite.bundled)
    implementation(project(":domain"))
    implementation(project(":data:repository"))
    implementation(project(":data:preferences"))
    // Dagger читает скомпилированные классы фич с compose-аннотациями — нужен runtime
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(project(":feature:account"))
    implementation(project(":feature:analysis"))
    implementation(project(":feature:categories"))
    implementation(project(":feature:expenses"))
    implementation(project(":feature:history"))
    implementation(project(":feature:income"))
    implementation(project(":feature:transaction"))
    implementation(project(":feature:settings"))
}
