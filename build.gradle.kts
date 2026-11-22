// Конфигурация модулей централизована в convention-плагинах build-logic
// (myfinances.android.library, myfinances.android.application, myfinances.compose,
//  myfinances.dagger, myfinances.room).
// ktlint/detekt применяются к модулям внутри android-конвенций; в корне — только
// kover для агрегированного отчёта по всем модулям (./gradlew koverHtmlReport).
plugins {
    id("myfinances.kover")
}

dependencies {
    kover(project(":app"))
    kover(project(":core:ui"))
    kover(project(":core:common"))
    kover(project(":core:di"))
    kover(project(":core:resources"))
    kover(project(":domain"))
    kover(project(":data:repository"))
    kover(project(":data:local"))
    kover(project(":data:preferences"))
    kover(project(":feature:account"))
    kover(project(":feature:income"))
    kover(project(":feature:expenses"))
    kover(project(":feature:transaction"))
    kover(project(":feature:history"))
    kover(project(":feature:settings"))
    kover(project(":feature:analysis"))
    kover(project(":feature:splash"))
    kover(project(":feature:categories"))
}
