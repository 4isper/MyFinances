import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        // Android-плагин применяется раньше myfinances.compose в build-файле модуля
        extensions.findByType(LibraryExtension::class.java)?.buildFeatures?.compose = true
        extensions.findByType(ApplicationExtension::class.java)?.buildFeatures?.compose = true

        dependencies {
            // platform() доступен на DependencyHandlerScope, а не на DependencyHandler
            DependencyHandlerScope.of(dependencies).apply {
                add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
                add("implementation", libs.findLibrary("androidx-material-icons-core").get())
                add("implementation", libs.findBundle("compose").get())
            }
        }
    }
}
