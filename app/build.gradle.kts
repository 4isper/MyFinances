plugins {
    id("myfinances.android.application")
    id("myfinances.compose")
    id("myfinances.dagger")
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
                "proguard-rules.pro"
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
    implementation(project(":data:remote"))
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    debugImplementation(libs.androidx.ui.tooling)
}
