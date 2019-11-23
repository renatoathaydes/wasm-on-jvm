# Compile C to WASM to JVM bytecode Example

This example illustrates two things:

* how to compile C code to WASM via Gradle.
* how to configure the build to customize package name and class names,
  regardless of the WASM file name and location.

See the [build.gradle](build.gradle) file for details.

## Requirements

This example requires that you have [LLVM](http://llvm.org/) installed as it will compile C code to WASM using
the `clang` command.

## Compiling

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

## Running

To run the `Main` class, which uses compiled C code, the easiest way is to create a fat jar first:

```bash
gradle fatJar
```

Then, simply run it with:

```bash
java -jar build/libs/wasm-example-1.0-SNAPSHOT.jar 2 3
```

Which should print `5` (showing that the C code is adding `2` and `3` as expected).

<hr/>

Notice that because this example does not have any external dependencies, using the jar produced by the `jar`
task would still work as well, so a fat jar is not necessary.

You can even run it by simply specifying where the class files are (as in the [hello-world](../hello-world) example)
and giving the qualified name of the main class:

```bash
java -cp build/classes/java/main:build/classes/wasm/main com.athaydes.wasm.c.Main 2 3 
```
