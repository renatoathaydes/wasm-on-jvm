package com.athaydes.wasm

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import asmble.cli.main as asmbleMain

fun main() {
    asmbleMain(arrayOf("compile", "examples/hello-world/src/main/wasm/add.wasm",
        "-format", "wasm", "-log", "info", "com.athaydes.Example"))
}

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
    }

}