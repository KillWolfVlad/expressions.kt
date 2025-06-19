package ru.killwolfvlad.expressions.examples

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.binaryOperators.BasePlusBinaryOperator
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EFunction
import java.math.BigDecimal
import kotlin.collections.plus

class BaseExampleTest : DescribeSpec({
    val defaultOptions = buildBaseExpressionOptions()
    val expressionExecutor = ExpressionExecutor(defaultOptions)

    it("comments and numbers") {
        expressionExecutor
            .execute(
                """
                # comments starts from #

                10 # this is number, also everything after # is comment and ignored
                10.25 # number can be with point
                10,25 # you also can use comma instead of point
                9 567,34 # number can contains spaces
                """.trimIndent(),
            ).value shouldBe BigDecimal("9567.34")
    }

    it("strings") {
        expressionExecutor
            .execute(
                """
                "this is string" # simple string
                "\" or \\ or \t or \r or \n" # you can escape some chars

                "string also can be multiline
                \r symbols always ignored in string"
                """.trimIndent(),
            ).value shouldBe "string also can be multiline\n\r symbols always ignored in string"
    }

    it("booleans") {
        expressionExecutor
            .execute(
                """
                true # use this reserved identifier for boolean true
                false # use this reserved identifier for boolean false
                # be careful, because parsing is case sensitivity
                """.trimIndent(),
            ).value shouldBe false
    }

    it("left unary operators") {
        expressionExecutor
            .execute(
                """
                -10 # plus left unary operator
                +10 # minus left unary operator
                !true # not left unary operator
                # left unary operator can be only one
                # left unary operator applies after right unary operator
                """.trimIndent(),
            ).value shouldBe false
    }

    it("binary operators") {
        expressionExecutor
            .execute(
                """
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
                """.trimIndent(),
            ).value shouldBe true
    }

    it("right unary operators") {
        expressionExecutor
            .execute(
                """
                10% # percent right unary operator
                # right unary operator can be only one
                # right unary operator has highest priority and applies first

                1000 + 20% # 1200, you can easily add percentages
                1000 - 20% # 800, you can easily subtract percentages

                # in other cases percent will be used as "percent / 100"
                1000 * 20% # 200
                1000 / 20% # 5000
                """.trimIndent(),
            ).value shouldBe BigDecimal("5000.00")
    }

    it("brackets") {
        expressionExecutor
            .execute(
                """
                -(10 + 20) # you can also use brackets
                +(3 * (-5 + 3)) # and brackets inside brackets
                """.trimIndent(),
            ).value shouldBe BigDecimal("-6.00")
    }

    it("function invocation and semicolons") {
        val customOptions =
            defaultOptions.copy(
                functions =
                    defaultOptions.functions +
                        listOf(
                            object : EFunction {
                                private val plusBinaryOperator = defaultOptions.binaryOperators.find { it is BasePlusBinaryOperator }!!

                                override val identifier = "sum"

                                override suspend fun execute(
                                    expressionExecutor: ExpressionExecutor,
                                    memory: EMemory,
                                    arguments: List<EInstance>,
                                ): EInstance =
                                    when (arguments.size) {
                                        0 -> defaultOptions.numberConstructor.createInstance(expressionExecutor, memory, "0")
                                        else ->
                                            arguments.reduce { a, b ->
                                                a.applyBinaryOperator(expressionExecutor, memory, b, plusBinaryOperator)
                                            }
                                    }
                            },
                        ),
            )

        val customExpressionExecutor = ExpressionExecutor(customOptions)

        customExpressionExecutor
            .execute(
                """
                sum() # invoke some built-in function without arguments (this function doesn't exists in base)
                sum(10; 20; 30) # invoke some built-in function with arguments (use ; to separate arguments)

                10; 20; 30 # you can also use ; to separate sub-expressions, last sub-expression will be used as result of execution
                # ; is not required in most cases - you can just use new line and ; will be inserted automatically

                10 +
                20 # if expression is not completed auto ; is disabled

                sum(10;
                    20;
                    30; # trailing ; also supported
                ) # inside brackets auto ; is disabled
                """.trimIndent(),
            ).value shouldBe BigDecimal("60.00")
    }

    it("built-in function - if") {
        expressionExecutor
            .execute(
                """
                if(true; 1; 0) # first argument - condition, second argument - then, third argument - else

                # if function support {} in condition, then and else and expand it when needed
                if({ 10 == 5 * 2 }; { 10 - 5 }; { 10 - "ab" }) # there will be no error because else branch will not be executed
                """.trimIndent(),
            ).value shouldBe BigDecimal("5.00")
    }

    it("built-in function - var") {
        shouldThrowExactly<EException> {
            expressionExecutor
                .execute(
                    """
                    # you can set variables
                    var("a"; 2)
                    var("b"; 3)

                    # and read it later
                    var("a") + var("b")

                    var("a"; 5) # you can also reassign variable, but it is not recommended, better create new variable
                    var("a"; "different type") # you can't reassign variable with different type
                    """.trimIndent(),
                )
        }.message shouldBe "var: can't reassign variable with different type!"
    }

    it("built-in function - fun") {
        expressionExecutor
            .execute(
                """
                var("a"; 4)

                # you can defined function
                fun("someFun"; "c"; "d"; { # place function body inside {}
                  # inside function you can also get access to other functions and variables in current visible scope
                  var("a"; var("a") + 1) # override global variable "a"
                  var("a") + var("b") + var("c") + var("d") # last sub-expression will be used as result of function
                })

                var("b"; 5)

                fun("someFun"; 2; 3) # 15, and invoke it later

                # you can also define function inside function
                fun("double and pow 2"; "a"; {
                  fun("double"; "a"; {
                    var("a") * 2 # function "double" has own argument "a" and use it
                  })

                  var("a"; fun("double"; var("a"))) # override argument "a" of function "double and pow 2", not global variable "a"

                  fun("pow2"; "a"; {
                    var("a") ** 2
                  })

                  var("c"; fun("pow2"; var("a"))) # create local variable "c"
                  var("c")
                })

                fun("double and pow 2"; 5) # you can use any string as function identifier, because it is string :)
                # but for built-in functions you can't use any identifier
                """.trimIndent(),
            ).value shouldBe BigDecimal("100.00")
    }

    it("types") {
        shouldThrowExactly<EException> {
            expressionExecutor
                .execute(
                    """
                    # language is dynamically typed, but strong typed under the hood
                    # you can't work with different types
                    10 + "2" # it is error

                    # you must use functions to explicit convert types
                    Number("10") # to convert string or boolean to number
                    String(10) # to convert number or boolean to string
                    Boolean(1) # to convert number or string to boolean
                    """.trimIndent(),
                )
        }.message shouldBe "BaseNumberInstance: argument type must be BaseNumberInstance!"
    }
})
