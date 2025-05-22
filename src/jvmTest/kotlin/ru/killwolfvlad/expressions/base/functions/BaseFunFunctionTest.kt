package ru.killwolfvlad.expressions.base.functions

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import java.math.BigDecimal

class BaseFunFunctionTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("must define function") {
        expressionExecutor
            .execute(
                """
                fun("sum"; "a"; "b"; {
                  var("a") + var("b")
                })
                """.trimIndent(),
            ).value shouldBe "sum"
    }

    it("must execute function") {
        expressionExecutor
            .execute(
                """
                fun("pow"; "a"; "b"; {
                  var("b") ** var("a")
                })

                fun("pow"; 2; 3)
                """.trimIndent(),
            ).value shouldBe BigDecimal("9.00")
    }

    it("must skip arguments mapping if passed more arguments that defined") {
        expressionExecutor
            .execute(
                """
                fun("sum"; "a"; "b"; {
                  var("a") + var("b")
                })

                fun("sum"; 3; 4; 5)
                """.trimIndent(),
            ).value shouldBe BigDecimal("7.00")
    }

    it("must execute function in function") {
        expressionExecutor
            .execute(
                """
                fun("sum"; "a"; "b"; {
                  var("a") + var("b")
                })

                fun("doubleAndPow2"; "a"; {
                  fun("sum"; var("a"); var("a")) ** 2
                })

                fun("doubleAndPow2"; 5)
                """.trimIndent(),
            ).value shouldBe BigDecimal("100.00")
    }

    it("must define and execute function in function") {
        expressionExecutor
            .execute(
                """
                fun("doubleAndPow2"; "a"; {
                  fun("sum"; "a"; "b"; {
                    var("a") + var("b")
                  })

                  fun("sum"; var("a"); var("a")) ** 2
                })

                fun("doubleAndPow2"; 5)
                """.trimIndent(),
            ).value shouldBe BigDecimal("100.00")
    }

    it("must use captured variables and functions") {
        expressionExecutor
            .execute(
                """
                var("a"; 1)

                fun("fun1"; {
                  fun("fun2"; {
                    fun("sum"; var("a") + 3; var("b") + 3)
                  })

                  var("a"; 5)

                  fun("fun2")
                })

                var("b"; 2)

                fun("sum"; "a"; "b"; {
                  var("a") + var("b")
                })

                fun("fun1")
                """.trimIndent(),
            ).value shouldBe BigDecimal("13.00")
    }

    describe("exceptions") {
        it("must throw exception if function called without arguments") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("fun()")
            }.message shouldBe "fun: arguments count can't be 0!"
        }

        it("must validate function name type") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("fun(1)")
            }.message shouldBe "fun: argument type must be BaseStringInstance!"
        }

        it("must throw exception if function is not defined and called with single argument") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute(
                    """
                    fun("sum")
                    """.trimIndent(),
                )
            }.message shouldBe "fun: arguments count can't be 1!"
        }

        it("must validate function argument name type") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute(
                    """
                    fun("sum"; 1; "")
                    """.trimIndent(),
                )
            }.message shouldBe "fun: argument type must be BaseStringInstance!"
        }

        it("must validate function body type") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute(
                    """
                    fun("sum"; "a"; 1)
                    """.trimIndent(),
                )
            }.message shouldBe "fun: argument type must be BaseStatementInstance!"
        }

        it("must throw exception when trying access to function from other scope") {
            shouldThrowExactly<EException> {
                expressionExecutor
                    .execute(
                        """
                        fun("fun1"; {
                          fun("sum"; 1; 2)
                        })

                        fun("fun2"; {
                          fun("sum"; "a"; "b"; {
                            var("a") + var("b")
                          })

                          fun("fun1")
                        })

                        fun("fun2")
                        """.trimIndent(),
                    )
            }.message shouldBe "fun: argument type must be BaseStringInstance!"
        }
    }
})
