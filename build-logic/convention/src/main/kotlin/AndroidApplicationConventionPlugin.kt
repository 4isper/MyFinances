import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")
        // статический анализ и покрытие — во всех модулях через конвенции
        pluginManager.apply("myfinances.ktlint")
        pluginManager.apply("myfinances.detekt")
        pluginManager.apply("myfinances.kover")

        extensions.configure<ApplicationExtension> {
            compileSdk = 37
            compileSdkMinor = 2

            defaultConfig {
                minSdk = 26
            }
        }

        dependencies {
            add("testImplementation", libs.findLibrary("junit").get())
        }
    }
}
