import java.util.Properties

plugins {
    id("myfinances.android.library")
}

android {
    namespace = "com.m4isper.domain"

    defaultConfig {
        buildConfigField("String", "API_TOKEN", "\"${getLocalProperty("API_TOKEN")}\"")
        buildConfigField("int", "ID_ACCOUNT", getLocalProperty("ID_ACCOUNT").ifEmpty { "0" })
    }

    buildFeatures {
        buildConfig = true
    }
}

private fun getLocalProperty(key: String): String {
    val properties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
    }
    return properties.getProperty(key, "")
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
