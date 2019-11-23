# Compile C to WASM to JVM bytecode Example

This example illustrates two things:

* how to compile C code to WASM via Gradle.
* how to configure the build to customize package name and class names,
  regardless of the WASM file name and location.

See the [build.gradle](build.gradle) file for details.

The `compileC` Gradle task invokes the LLVM compiler to compile C files in
[src/main/c](src/main/c) to WASM binaries.

The WASM files are saved at `build/compiled-wasm`.

After that, the `compileWasm` task, added by this plugin, compiles the WASM files to Java class
files, putting them in the standard location `build/classes/wasm/main`.

This directory is automatically added to the `implementation` dependencies of the Gradle project when
this plugin is applied to it, so the classes are "visible" to Java (and other JVM languages) source code.

To compile everything, run:

```bash
gradle compileJava
```

The `compileWasm` task is run automatically.

To run the `Main` class, which uses compiled C code, the easiest way is to create a fat jar first:

```bash
gradle fatJar
```

Then, simply run it with:

```bash
java -jar build/libs/wasm-example-1.0-SNAPSHOT.jar 2 3
```

Which should print `5` (showing that the C code is adding `2` and `3` as expected).
