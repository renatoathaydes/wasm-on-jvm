package com.athaydes.wasm

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.AbstractCompile

open class WasmExtension {
    var sourceDir: Any = "src/main/wasm"
    var packageName: String = ""
    var classNameByFile: Map<String, String> = mutableMapOf()
}

class WasmGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (project.configurations.findByName("implementation") == null) {
            throw GradleException(
                    "Cannot apply com.athaydes.wasm plugin because the 'implementation' configuration " +
                            "does not exist. Please apply the 'java' plugin (or another JVM plugin) before applying it."
            )
        }

        project.extensions.create("wasm", WasmExtension::class.java)
        val compileWasm = project.tasks.create("compileWasm", CompileWasmTask::class.java)

        project.tasks.withType(AbstractCompile::class.java) {
            it.dependsOn(compileWasm)
        }

        project.dependencies.add("implementation", project.files(compileWasm.outputDir))

        val sourceSet = project.extensions.getByType(SourceSetContainer::class.java)
        sourceSet.getByName("main").resources.srcDir(compileWasm.outputDir)
    }

}
