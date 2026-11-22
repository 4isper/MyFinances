dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        // ktlint/detekt/kover публикуют plugin-маркеры в Gradle Plugin Portal
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
