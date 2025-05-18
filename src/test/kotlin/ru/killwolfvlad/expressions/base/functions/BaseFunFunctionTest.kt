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
                fun("sum"; "a"; "b"; {
                  var("a") + var("b")
                })

                fun("sum"; 3; 4)
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
    }
})
