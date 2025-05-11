package ru.killwolfvlad.expressions.base.classes

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator
import java.math.BigDecimal

class BaseNumberInstanceTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("20 = 20") {
        expressionExecutor.execute("20") shouldBe BigDecimal("20.00")
    }

    it("20.25 = 20.25") {
        expressionExecutor.execute("20.25") shouldBe BigDecimal("20.25")
    }

    describe("BasePlusBinaryOperator") {
        it("20 + 30 = 50") {
            expressionExecutor.execute("20 + 30") shouldBe BigDecimal("50.00")
        }

        it("20.25 + 30.33 = 50") {
            expressionExecutor.execute("20.25 + 30.33") shouldBe BigDecimal("50.58")
        }
    }

    describe("BaseMinusBinaryOperator") {
        it("20 - 30 = -10") {
            expressionExecutor.execute("20 - 30") shouldBe BigDecimal("-10.00")
        }

        it("20.25 - 30.33 = 50") {
            expressionExecutor.execute("20.25 - 30.33") shouldBe BigDecimal("-10.08")
        }
    }

    describe("BaseMultiplyBinaryOperator") {
        it("20 * 30 = 600") {
            expressionExecutor.execute("20 * 30") shouldBe BigDecimal("600.00")
        }

        it("20.25 * 30.33 = 614.18") {
            expressionExecutor.execute("20.25 * 30.33") shouldBe BigDecimal("614.18")
        }
    }

    describe("BaseDivideBinaryOperator") {
        it("20 / 30 = 0.67") {
            expressionExecutor.execute("20 / 30") shouldBe BigDecimal("0.67")
        }

        it("20.25 / 30.33 = 0.67") {
            expressionExecutor.execute("20.25 / 30.33") shouldBe BigDecimal("0.67")
        }
    }

    describe("BaseExponentiationBinaryOperator") {
        it("2 ** 3 = 8") {
            expressionExecutor.execute("2 ** 3") shouldBe BigDecimal("8.00")
        }

        it("2.25 ** 3.33") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("2.25 ** 3.33")
            }.message shouldBe "BaseNumberInstance: pow must be integer!"
        }

        it("-2 ** 3 = -8") {
            expressionExecutor.execute("-2 ** 3") shouldBe BigDecimal("-8.00")
        }

        it("2 ** -3 = -8") {
            expressionExecutor.execute("-2 ** 3") shouldBe BigDecimal("-8.00")
        }

        it("2 ** 3 ** 2 = 64") {
            expressionExecutor.execute("2 ** 3 ** 2") shouldBe BigDecimal("64.00")
        }
    }

    describe("BaseGreaterBinaryOperator") {
        it("20 > 20 = false") {
            expressionExecutor.execute("20 > 20") shouldBe false
        }

        it("20.25 > 20.25 = false") {
            expressionExecutor.execute("20.25 > 20.25") shouldBe false
        }

        it("20 > 30 = false") {
            expressionExecutor.execute("20 > 30") shouldBe false
        }

        it("30 > 20 = true") {
            expressionExecutor.execute("30 > 20") shouldBe true
        }

        it("20.25 > 30.33 = false") {
            expressionExecutor.execute("20.25 > 30.33") shouldBe false
        }

        it("30.33 > 20.25 = true") {
            expressionExecutor.execute("30.33 > 20.25") shouldBe true
        }
    }

    describe("BaseGreaterOrEqualBinaryOperator") {
        it("20 >= 20 = true") {
            expressionExecutor.execute("20 >= 20") shouldBe true
        }

        it("20.25 >= 20.25 = true") {
            expressionExecutor.execute("20.25 >= 20.25") shouldBe true
        }

        it("20 >= 30 = false") {
            expressionExecutor.execute("20 >= 30") shouldBe false
        }

        it("30 >= 20 = true") {
            expressionExecutor.execute("30 >= 20") shouldBe true
        }

        it("20.25 >= 30.33 = false") {
            expressionExecutor.execute("20.25 >= 30.33") shouldBe false
        }

        it("30.33 >= 20.25 = true") {
            expressionExecutor.execute("30.33 >= 20.25") shouldBe true
        }
    }

    describe("BaseLessBinaryOperator") {
        it("20 < 20 = false") {
            expressionExecutor.execute("20 < 20") shouldBe false
        }

        it("20.25 < 20.25 = false") {
            expressionExecutor.execute("20.25 < 20.25") shouldBe false
        }

        it("20 < 30 = true") {
            expressionExecutor.execute("20 < 30") shouldBe true
        }

        it("30 < 20 = false") {
            expressionExecutor.execute("30 < 20") shouldBe false
        }

        it("20.25 < 30.33 = true") {
            expressionExecutor.execute("20.25 < 30.33") shouldBe true
        }

        it("30.33 < 20.25 = false") {
            expressionExecutor.execute("30.33 < 20.25") shouldBe false
        }
    }

    describe("BaseLessOrEqualBinaryOperator") {
        it("20 <= 20 = true") {
            expressionExecutor.execute("20 <= 20") shouldBe true
        }

        it("20.25 <= 20.25 = true") {
            expressionExecutor.execute("20.25 <= 20.25") shouldBe true
        }

        it("20 <= 30 = true") {
            expressionExecutor.execute("20 <= 30") shouldBe true
        }

        it("30 <= 20 = false") {
            expressionExecutor.execute("30 <= 20") shouldBe false
        }

        it("20.25 <= 30.33 = true") {
            expressionExecutor.execute("20.25 <= 30.33") shouldBe true
        }

        it("30.33 <= 20.25 = false") {
            expressionExecutor.execute("30.33 <= 20.25") shouldBe false
        }
    }

    describe("BaseEqualBinaryOperator") {
        it("20 == 20 = true") {
            expressionExecutor.execute("20 == 20") shouldBe true
        }

        it("20.25 == 20.25 = true") {
            expressionExecutor.execute("20.25 == 20.25") shouldBe true
        }

        it("20 == 30 = false") {
            expressionExecutor.execute("20 == 30") shouldBe false
        }
    }

    describe("BaseNotEqualBinaryOperator") {
        it("20 != 20 = false") {
            expressionExecutor.execute("20 != 20") shouldBe false
        }

        it("20.25 != 20.25 = false") {
            expressionExecutor.execute("20.25 != 20.25") shouldBe false
        }

        it("20 != 30 = true") {
            expressionExecutor.execute("20 != 30") shouldBe true
        }
    }

    describe("BasePlusLeftUnaryOperator") {
        it("+20 = 20") {
            expressionExecutor.execute("+20") shouldBe BigDecimal("20.00")
        }

        it("+20.25 = 20.25") {
            expressionExecutor.execute("+20.25") shouldBe BigDecimal("20.25")
        }
    }

    describe("BaseMinusLeftUnaryOperator") {
        it("-20 = -20") {
            expressionExecutor.execute("-20") shouldBe BigDecimal("-20.00")
        }

        it("-20.25 = -20.25") {
            expressionExecutor.execute("-20.25") shouldBe BigDecimal("-20.25")
        }
    }

    describe("exceptions") {
        it("must throw argument type must be BaseNumberInstance exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("20 + true")
            }.message shouldBe "BaseNumberInstance: argument type must be BaseNumberInstance!"
        }

        it("must throw unsupported binary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("20 && 30")
            }.message shouldBe "BaseNumberInstance: unsupported binary operator type BaseAndBinaryOperator!"
        }

        it("must throw unsupported left unary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("!20")
            }.message shouldBe "BaseNumberInstance: unsupported left unary operator type BaseNotLeftUnaryOperator!"
        }

        it("must throw unsupported right unary operator exception") {
            class CustomRightUnaryOperator : ERightUnaryOperator {
                override val description = ""

                override val identifier = "$"
            }

            val customExpressionExecutor =
                ExpressionExecutor(
                    buildBaseExpressionOptions().copy(
                        rightUnaryOperators = listOf(CustomRightUnaryOperator()),
                    ),
                )

            shouldThrowExactly<EException> {
                customExpressionExecutor.execute("20$")
            }.message shouldBe "BaseNumberInstance: unsupported right unary operator type CustomRightUnaryOperator!"
        }
    }
})
