package ru.killwolfvlad.expressions.core

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import ru.killwolfvlad.expressions.base.binaryOperators.BasePlusBinaryOperator
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EFunction
import ru.killwolfvlad.expressions.core.tokens.EPrimitiveToken
import java.math.BigDecimal
import kotlin.collections.plus

class ExpressionExecutorTest : DescribeSpec({
    val defaultOptions = buildBaseExpressionOptions()

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

    val expressionExecutor = ExpressionExecutor(customOptions)

    describe("semicolon") {
        it("must apply all operators while operators is not empty") {
            expressionExecutor.execute("1 + 2 + 3; 7").value shouldBe BigDecimal("7.00")
        }

        it("must apply all operators while operator is not open bracket") {
            expressionExecutor
                .execute(
                    """
                    var("a" + "b" + "c"; 5)
                    var("abc")
                    """.trimIndent(),
                ).value shouldBe BigDecimal("5.00")
        }

        it("must apply all operators while operator is not semicolon") {
            expressionExecutor.execute("if(true; 1 + 2 + 3; 0)").value shouldBe BigDecimal("6.00")
        }
    }

    describe("close bracket") {
        it("must apply all operators while operator is not open bracket") {
            expressionExecutor.execute("-(1+2+3)").value shouldBe BigDecimal("-6.00")
        }

        it("must apply all operators while operator is not semicolon") {
            expressionExecutor.execute("if(!true; 0; 1 + 2 + 3)").value shouldBe BigDecimal("6.00")
        }

        describe("when previous operator is open bracket") {
            it("must remove open bracket if it is single operator") {
                expressionExecutor.execute("(10+20)").value shouldBe BigDecimal("30.00")
            }

            it("must remove open bracket if operator before open bracket is not function") {
                expressionExecutor.execute("-(10+20)").value shouldBe BigDecimal("-30.00")
            }

            describe("when operator before open bracket is function") {
                it("must execute function if function without arguments") {
                    expressionExecutor.execute("sum()").value shouldBe BigDecimal("0.00")
                }

                it("must execute function with single argument") {
                    expressionExecutor.execute("sum(10)").value shouldBe BigDecimal("10.00")
                }
            }
        }

        describe("when previous operator is semicolon") {
            it("must execute function with two argument") {
                expressionExecutor
                    .execute(
                        """
                        sum("a"; "b")
                        """.trimIndent(),
                    ).value shouldBe "ab"
            }

            it("must execute function with tree arguments") {
                expressionExecutor
                    .execute(
                        """
                        sum("a"; "b"; "c")
                        """.trimIndent(),
                    ).value shouldBe "abc"
            }

            it("must throw exception if function name missing after expression start") {
                shouldThrowExactly<EException> {
                    expressionExecutor.execute("(10; 30)")
                }.message shouldContain "ExpressionExecutor: missing function name!"
            }

            it("must throw exception if function name missing after other operator") {
                shouldThrowExactly<EException> {
                    expressionExecutor.execute("-(10; 30)")
                }.message shouldContain "ExpressionExecutor: missing function name!"
            }
        }
    }

    describe("primitive") {
        it("must create number instance") {
            expressionExecutor.execute("30").value shouldBe BigDecimal("30.00")
        }

        it("must create string instance") {
            expressionExecutor.execute("\"this is string\"").value shouldBe "this is string"
        }

        it("must create boolean instance for true") {
            expressionExecutor.execute("true").value shouldBe true
        }

        it("must create boolean instance for false") {
            expressionExecutor.execute("false").value shouldBe false
        }

        it("must create statement instance") {
            expressionExecutor.execute("{30}").value shouldBe
                listOf(
                    EPrimitiveToken("30", customOptions.numberConstructor),
                )
        }
    }

    describe("binary operator") {
        it("must apply left unary operator first") {
            expressionExecutor.execute("-2 ** 3").value shouldBe BigDecimal("-8.00")
        }

        it("must use right to left associativity for exponentiation") {
            expressionExecutor.execute("2 ** 3 ** 2").value shouldBe BigDecimal("512.00")
        }

        it("must apply exponentiation before multiply") {
            expressionExecutor.execute("2 * 2 ** 3").value shouldBe BigDecimal("16.00")
        }

        it("must apply multiply before plus") {
            expressionExecutor.execute("2 + 10 / 2").value shouldBe BigDecimal("7.00")
        }

        it("must apply plus before compare") {
            expressionExecutor.execute("10 > 2 * 7").value shouldBe false
        }

        it("must apply compare before equal") {
            expressionExecutor.execute("true == 10 > 5").value shouldBe true
        }

        it("must apply equal before and") {
            expressionExecutor.execute("true && 10 > 5").value shouldBe true
        }

        it("must apply and before or") {
            expressionExecutor.execute("false || true && true").value shouldBe true
        }
    }

    describe("right unary operator") {
        it("must apply right unary operator") {
            expressionExecutor.execute("20%").value shouldBe BigDecimal("0.2000")
        }
    }

    it("must apply all operators while operators is not empty") {
        expressionExecutor.execute("1 + 2 + 3").value shouldBe BigDecimal("6.00")
    }

    it("must apply all operators while operator is not semicolon") {
        expressionExecutor.execute("0; 1 + 2 + 3").value shouldBe BigDecimal("6.00")
    }

    it("must execute tokens") {
        expressionExecutor
            .execute(
                expressionExecutor.parser.parse("20 + 30"),
            ).value shouldBe BigDecimal("50.00")
    }
})
