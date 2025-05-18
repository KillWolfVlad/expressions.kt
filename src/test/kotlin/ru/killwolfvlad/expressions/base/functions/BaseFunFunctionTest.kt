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

    it("must define and execute multiple functions in function") {
        expressionExecutor
            .execute(
                """
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

                 fun("double and pow 2"; 5)
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

    it("eval") {
        expressionExecutor
            .execute(
                """
                fun("eval"; "s"; {
                  if(true; Statement(var("s")); 0)
                })

                fun("eval"; "10 + 20")
                """.trimIndent(),
            ).value shouldBe BigDecimal("30.00")
    }

    describe("fibonacci") {
        val baseExpression =
            """
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
            """.trimIndent()

        val testCases =
            listOf(
                -30 to -832040,
                -25 to 75025,
                -10 to -55,
                -9 to 34,
                -8 to -21,
                -7 to 13,
                -6 to -8,
                -5 to 5,
                -4 to -3,
                -3 to 2,
                -2 to -1,
                -1 to 1,
                0 to 0,
                1 to 1,
                2 to 1,
                3 to 2,
                4 to 3,
                5 to 5,
                6 to 8,
                7 to 13,
                8 to 21,
                9 to 34,
                10 to 55,
                11 to 89,
                12 to 144,
                13 to 233,
                14 to 377,
                25 to 75025,
                30 to 832040,
            )

        testCases.forEach { testCase ->
            it("fun(\"fibonacci\"; ${testCase.first}) = ${testCase.second}") {
                expressionExecutor
                    .execute(
                        """
                        $baseExpression

                        fun("fibonacci"; ${testCase.first})
                        """.trimIndent(),
                    ).value
                    .let { it as BigDecimal }
                    .toInt() shouldBe testCase.second
            }
        }
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
