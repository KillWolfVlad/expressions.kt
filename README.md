# expressions.kt

<p align="center">
  <img src="./brand/logo.jpg" width="250"/>
  <br>
</p>

Embedded Extendable Expression Language for Kotlin.

> Inspired by Excel, Clojure, JavaScript and Kotlin

## Features

- Built-in primitives support: number, string and boolean with your own implementation under the hood (e.g. use double or BigDecimal for numbers)
- Extend expressions with your own binary, left and right unary operators, types and functions
- With memory you can add support of variables and user functions
- Parse expression and work with tokens
- Powerful interop with Kotlin and fully coroutines (suspend) support

## Install

See also [official docs](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package) about using GitHub Packages.

`build.gradle.kts`

```kotlin
plugins {
    id("net.saliman.properties") version "1.5.2"
}

repositories {
    mavenCentral()

    maven {
        url = uri("https://maven.pkg.github.com/KillWolfVlad/expressions.kt")

        credentials {
            username = project.findProperty("gpr.user") as String?
            password = project.findProperty("gpr.key") as String?
        }
    }
}

dependencies {
    implementation("ru.killwolfvlad:expressions:version")
}
```

`gradle-local.properties`

```properties
gpr.user=xxx
gpr.key=xxx
```

You can find latest version in [GitHub Packages](https://github.com/KillWolfVlad/expressions.kt/packages/2503335).

> WARNING! Don' forget add `gradle-local.properties` to `.gitignore`

## Usage

- [Base example](./src/test/kotlin/ru/killwolfvlad/expressions/examples/BaseExampleTest.kt)
- [Factorial example](./src/test/kotlin/ru/killwolfvlad/expressions/examples/FactorialExampleTest.kt)
- [Fibonacci example](./src/test/kotlin/ru/killwolfvlad/expressions/examples/FibonacciExampleTest.kt)
- [Eval example](./src/test/kotlin/ru/killwolfvlad/expressions/examples/EvalExampleTest.kt)

## Supported platforms

- Java v21+ LTS
- Kotlin v1.9+

## Maintainers

- [@KillWolfVlad](https://github.com/KillWolfVlad)

## License

This repository is released under version 2.0 of the
[Apache License](https://www.apache.org/licenses/LICENSE-2.0).
