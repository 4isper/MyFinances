pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyFinances"
include(":app")
include(":core:ui", ":core:common", ":core:di")
include(":domain")
include(":data:repository", ":data:local", ":data:remote")

include(":feature:account")
include(":feature:income")
include(":feature:expenses")
include(":feature:transaction")
include(":feature:history")
include(":feature:settings")
include(":feature:analysis")
include(":feature:splash")
include(":feature:categories")
include(":core:resources")
include(":data:preferences")
