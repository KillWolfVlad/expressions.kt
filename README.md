# expressions.kt

<p align="center">
  <img src="./brand/logo.jpg" width="250"/>
  <br>
</p>

Embedded Expression Language for Kotlin.

> Expression Language inspired by Excel, Clojure, JavaScript and Kotlin.

## Features

- Built-in primitives support: number, string and boolean with your own implementation under the hood (e.g. use double or BigDecimal for numbers)
- Extend expressions with your own binary, left and right unary operators, classes and functions
- With memory you can add support of variables and user functions
- Parse expression and work with tokens
- Powerful interop with Kotlin with fully coroutines (suspend) support

## Example

> WARNING! Example includes features from [base](./src/main/kotlin/ru/killwolfvlad/expressions/base), you can modify it or create your own

```python
# comments starts from #

10 # this is number, also everything after # is comment and ignored
10.25 # number can be with point
10,25 # you also can use comma instead of point

"this is string" # simple string
"\" or \\ or \t or \r or \n" # you can escape some chars

"string also can be multiline
\r symbols always ignored in string"

true # use this reserved identifier for boolean true
false # use this reserved identifier for boolean false
# be careful, because parsing is case sensitivity

-10 # plus left unary operator
+10 # minus left unary operator
!true # not left unary operator
# left unary operator can be only one
# left unary operator applies after right unary operator

10 + 20 # plus binary operator
10 - 20 # minus binary operator
10 / 20 # divide binary operator
10 * 20 # multiply binary operator
2 ** 3 # exponential binary operator, has right to left association, so 2 ** 3 ** 2 = 512
true && false # and binary operator
true || false # or binary operator
10 > 20 # greater binary operator
10 >= 20 # greater or qual binary operator
10 < 20 # less binary operator
10 <= 20 # less or qual binary operator
10 == 20 # qual binary operator
10 != 20 # not qual binary operator
# binary operator applies after left unary operator
# binary operator priority - 1) exponentiation (**)
#                            2) multiply (*, /)
#                            3) plus (+, -)
#                            4) compare (>, >=, <, <=)
#                            5) equal (==, !=)
#                            6) and (&&)
#                            7) or (||)

10% # percent right unary operator
# right unary operator can be only one
# right unary operator has highest priority

1000 + 20% # 1200, you can easily add percentages
1000 - 20% # 800, you can easily subtract percentages

# in other cases percent will be used as "percent / 100"
1000 * 20% # 200
1000 / 20% # 500

-(10 + 20) # you can also use brackets
+(3 * (2 + 3)) # and brackets inside brackets

someFun() # invoke some built-in function without arguments (this function doesn't exists in base)
someFun(10; 20; 30) # invoke some built-in function with arguments (use ; to separate arguments)

10; 20; 30 # you can also ; to separate sub-expressions, last sub-expression will be used as result of execution
# ; is not required in most cases - you can just use new line and ; will be inserted automatically

10 +
20 # if expression is not completed auto ; is disabled

someFun(10;
        20;
        30; # trailing ; also supported
) # inside brackets auto ; is disabled

# built-in function - if
if(true; 1; 2) # first argument - condition, second argument - then, third argument - else

# built-in function - var
# you can set variables
var("a"; 2)
var("b"; 3)

# and read it later
var("a") + var("b")

var("a"; 5) # you also can reassign variable, but it is not recommended, better create new variable
var("a"; "different type") # you can't reassign variable with different type

# built-in function - fun
# you can defined function
fun("sum"; "a"; "b"; { # place expression body inside {}
  var("a") + var("b") # every function is pure and can read only their arguments
})

fun("sum"; 2; 3) # and invoke it later

# you can also define function inside function, but remember that every function is pure
fun("double and pow 2"; "a"; {
  fun("double"; "a"; {
    var("a") * 2
  })

  var("a"; fun("double"; var("a")))

  fun("pow2"; "a"; {
    var("a") ** 2
  })

  fun("pow2"; var("a"))
})

fun("double and pow 2"; 5) # you can use any string as function identifier, because it is string :)
# but for built-in function you can't use any identifier

# language is dynamically typed, but strong typed under the under the hood
# you can't work with different types
10 + "2" # it is error

# you must use functions to explicit convert types
Number("10") # to convert string or boolean to number
String(10) # to convert number or boolean to string
Boolean(1) # to convert number or string to boolean
```

### Fibonacci

> NOTE: `if` function support `{}` in `then` and `else` and expand it when needed.

```python
fun("fibonacci"; "n"; {
  if(var("n") < 0; {
    -1 ** (-var("n") + 1) * fun("fibonacci"; -var("n"))
    }; {
    if(var("n") == 0;
      0; {
      if(var("n") == 1;
        1; {
        fun("fibonacci"; var("n") - 1) + fun("fibonacci"; var("n") - 2)
      })
    })
  })
})

fun("fibonacci"; 10)
```

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

See [base](./src/main/kotlin/ru/killwolfvlad/expressions/base) and [base tests](./src/test/kotlin/ru/killwolfvlad/expressions/base) for usage example.

## Docs

- [Reverse Polish notation algorithm](http://e-maxx.ru/algo/expressions_parsing)

## Supported platforms

- Java v21+ LTS
- Kotlin v1.9+

## Maintainers

- [@KillWolfVlad](https://github.com/KillWolfVlad)

## License

This repository is released under version 2.0 of the
[Apache License](https://www.apache.org/licenses/LICENSE-2.0).
