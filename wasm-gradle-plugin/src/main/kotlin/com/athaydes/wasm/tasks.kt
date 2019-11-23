package com.athaydes.wasm

import asmble.cli.Compile
import asmble.util.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class CompileWasmTask : DefaultTask() {

    val ext: WasmExtension by lazy { project.extensions.getByName("wasm") as WasmExtension }

    @get:InputDirectory
    val inputDir: File by lazy { project.file(ext.sourceDir) }

    @get:OutputDirectory
    val outputDir: File by lazy { project.file("${project.buildDir}/classes/wasm/main") }

    @TaskAction
    fun compile() {
        outputDir.mkdirs()
        val sourceDir = inputDir
        if (sourceDir.isDirectory) {
            sourceDir.walk(FileWalkDirection.TOP_DOWN).filter { it.isFile }.forEach { file ->
                var fullClassName = ext.classNameByFile[file.path] ?: if (ext.packageName.isBlank())
                    file.nameWithoutExtension
                else "${ext.packageName}.${file.nameWithoutExtension}"

                val (packages, className) = {
                    val parts = fullClassName.split(".").filter { it.isNotBlank() }
                    parts.dropLast(1) to parts.last()
                }()

                val classFile = "${outputDir}/" +
                        (if (packages.isNotEmpty()) packages.joinToString("/", postfix = "/") else "") +
                        "${className}.class"

                File(classFile).parentFile.mkdirs()

                logger.debug("Compiling wasm file $file to class $fullClassName")

                Compile().let { cmd ->
                    cmd.logger = Logger.Print(Logger.Level.WARN)
                    cmd.run(
                        Compile.Args(
                            file.absolutePath, "<use file extension>",
                            fullClassName, classFile, null, false
                        )
                    )
                }
            }
        } else {
            logger.warn("No WASM sources found, sourceDir is no a directory: $sourceDir")
        }
    }

}
