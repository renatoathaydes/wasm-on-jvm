# WASM-on-JVM Gradle Plugin

This is a [Gradle Plugin](https://docs.gradle.org/current/userguide/plugins.html) that lets Java developers
use [Web Assembly](https://webassembly.org/) (WASM) in their project with minimal friction,
whether it's WASM text files [written by hand](https://blog.scottlogic.com/2018/04/26/webassembly-by-hand.html),
or binaries compiled with [WASM compilers](https://github.com/appcypher/awesome-wasm-langs).

Compilation from WASM to JVM bytecode is performed by the [cretz/asmble](https://github.com/cretz/asmble) project.

## How to use

Apply this plugin to your Gradle project:

```groovy
plugins {
    id 'com.athaydes.wasm'
}
```

Add `.wasm` (binary) or `wast` (text) WASM files to the `src/main/wasm` directory.

Compile them with `gradle compileWasm`.

Use the generated classes in your Java code.

Compile everything and run it!

## Configuration

Optionally, you can configure how the WASM Plugin behaves:

```groovy
wasm {
    // where to find wasm files
    // (this value is the default, so omit if you don't want to change it)
    sourceDir = file('src/main/wasm')

    // this is used as the top-level package name for this project.
    packageName = 'org.mypkg'

    // mappings between WASM file names and desired Java class names.
    // By default, WASM files are compiled into Java classes with the same name as the files,
    // without their extension.
    classNameByFile = [
            'add.wasm': 'WasmAdder'
    ]
}
```

## Examples

See the [hello-world](examples/hello-world) example to get started real quick!

And the [C-to-WASM-to-JVM](examples/configured-c-to-wasm) example for more advanced usage, such as
compiling C files to WASM, then using them from Java or other JVM languages, and running it with a single,
fat jar.
