package com.athaydes.wasm

import asmble.cli.Compile
import asmble.util.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class CompileWasmTask : DefaultTask() {

    private data class ClassName(val qualified: String, val simple: String, val packages: List<String>)

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
                val className = resolvePkgAndClassName(file)
                val classFile = classFileFor(className)

                classFile.parentFile?.mkdirs()

                logger.warn("Compiling wasm file $file to class ${className.qualified}")

                Compile().let { cmd ->
                    cmd.logger = Logger.Print(Logger.Level.WARN)
                    cmd.run(
                            Compile.Args(
                                    file.absolutePath, "<use file extension>",
                                    className.qualified, classFile.path, null, false
                            )
                    )
                }
            }
        } else {
            logger.warn("No WASM sources found, sourceDir is not a directory: $sourceDir")
        }
    }

    private fun resolvePkgAndClassName(file: File): ClassName {
        val sourceDir = inputDir

        val relativeClassName = file.relativeTo(sourceDir).path

        val customName = ext.classNameByFile[relativeClassName]

        val fullClassName = joinClassNameWithPkg(customName ?: file.nameWithoutExtension)

        val parts = fullClassName.split(".").filter { it.isNotBlank() }
        return ClassName(
                qualified = fullClassName,
                simple = parts.last(),
                packages = parts.dropLast(1)
        )
    }

    private fun joinClassNameWithPkg(name: String): String {
        return if (ext.packageName.isBlank()) name else
            "${ext.packageName}.${name}"
    }

    private fun classFileFor(name: ClassName): File {
        if (name.packages.isEmpty()) {
            return File(outputDir, "${name.simple}.class")
        }

        val dir = name.packages.joinToString(File.separator, postfix = File.separator)

        return File(outputDir, "${dir}${name.simple}.class")
    }

}
