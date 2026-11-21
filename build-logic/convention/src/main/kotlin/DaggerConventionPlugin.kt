import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class DaggerConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")

        extensions.configure<KspExtension> {
            arg("dagger.fullBindingGraphValidation", "WARNING")
        }

        dependencies {
            add("implementation", libs.findLibrary("dagger").get())
            add("ksp", libs.findLibrary("dagger-compiler").get())
        }
    }
}
