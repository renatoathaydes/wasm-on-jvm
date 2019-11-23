# Hello World Example

Simplest possible example using the Gradle WASM plugin.

There are only 2 source files:

* [src/main/java/Hello.java](src/main/java/Hello.java)
* [src/main/wasm/HelloWasm.wast](src/main/wasm/HelloWasm.wast)

To compile, run:

```bash
gradle compileJava
```

The `compileWasm` task is run automatically.

To run the `Hello` class, which uses the WASM-derived `HelloWasm` class, run:

```bash
java -cp build/classes/java/main:build/classes/wasm/main Hello
```
