import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Чистый Kotlin JVM-модуль без Android-машинерии: домен, утилиты, тестовые фейки.
 * Компилятор сам запрещает android.* и зависимости от android-модулей.
 */
class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        pluginManager.apply("myfinances.ktlint")
        pluginManager.apply("myfinances.detekt")
        pluginManager.apply("myfinances.kover")

        dependencies {
            add("testImplementation", libs.findLibrary("junit").get())
            add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
        }
    }
}
