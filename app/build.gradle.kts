plugins {
    id("myfinances.android.application")
    id("myfinances.compose")
    id("myfinances.dagger")
    // NavKey-маршруты сериализуются для персистентности бэкстека
    alias(libs.plugins.kotlin.serialization)
}

val appFileName = "MyFinances"

// versionName ведётся руками, versionCode — число коммитов: растёт сам, ручной
// бамп не нужен. Rebase или squash уменьшают счётчик, и следующий билд получит
// меньший versionCode, чем уже выпущенный, — после переписывания истории
// проверить счётчик перед релизом.
val appVersionName = "1.0.0"

fun git(vararg args: String): String =
    providers
        .exec {
            workingDir(rootDir)
            commandLine("git", *args)
        }.standardOutput.asText
        .get()
        .trim()

val commitCount = git("rev-list", "--count", "HEAD").toInt()
val commitHash = git("rev-parse", "--short", "HEAD")

android {
    namespace = "com.m4isper.myfinances"

    defaultConfig {
        applicationId = "com.m4isper.myfinances"
        targetSdk = 37
        versionCode = commitCount
        versionName = appVersionName
    }

    buildTypes {
        debug {
            // Суффикс виден в настройках телефона: сразу ясно, что стоит не
            // релиз, и виден коммит этой сборки.
            versionNameSuffix = "-dev-$commitHash"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            output.outputFileName.set(
                "$appFileName-${variant.name}-$appVersionName-$commitCount-$commitHash.apk",
            )
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":data:repository"))
    implementation(project(":data:local"))
    implementation(project(":data:preferences"))
    implementation(project(":domain"))
    implementation(project(":core:di"))
    implementation(project(":feature:account"))
    implementation(project(":feature:analysis"))
    implementation(project(":feature:categories"))
    implementation(project(":feature:expenses"))
    implementation(project(":feature:history"))
    implementation(project(":feature:income"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:transaction"))
    implementation(project(":feature:settings"))
    implementation(project(":core:resources"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Navigation 3 + Material3 Adaptive
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.adaptive.layout)
    implementation(libs.androidx.adaptive.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.kotlinx.serialization.core)
    debugImplementation(libs.androidx.ui.tooling)
}
