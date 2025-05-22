package ru.killwolfvlad.expressions.base.primitives

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import java.math.BigDecimal

class BasePercentInstanceTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("20% = 0.2000") {
        expressionExecutor.execute("20%").value shouldBe BigDecimal("0.2000")
    }

    it("20.25% = 0.2025") {
        expressionExecutor.execute("20.25%").value shouldBe BigDecimal("0.2025")
    }

    it("1000 + 20% * 4 = 1000.80") {
        expressionExecutor.execute("1000 + 20% * 4").value shouldBe BigDecimal("1000.80")
    }

    describe("BasePlusBinaryOperator") {
        it("1000 + 20% = 1200") {
            expressionExecutor.execute("1000 + 20%").value shouldBe BigDecimal("1200.00")
        }

        it("1000 + 20.25% = 1202.50") {
            expressionExecutor.execute("1000 + 20.25%").value shouldBe BigDecimal("1202.50")
        }

        it("20% + 1000 = 1000.20") {
            expressionExecutor.execute("20% + 1000").value shouldBe BigDecimal("1000.20")
        }

        it("20% + 20% = 0.24") {
            expressionExecutor.execute("20% + 20%").value shouldBe BigDecimal("0.24")
        }
    }

    describe("BaseMinusBinaryOperator") {
        it("1000 - 20% = 800") {
            expressionExecutor.execute("1000 - 20%").value shouldBe BigDecimal("800.00")
        }

        it("1000 - 20.25% = 797.50") {
            expressionExecutor.execute("1000 - 20.25%").value shouldBe BigDecimal("797.50")
        }

        it("20% - 1000 = -999.80") {
            expressionExecutor.execute("20% - 1000").value shouldBe BigDecimal("-999.80")
        }

        it("20% - 20% = 0.16") {
            expressionExecutor.execute("20% - 20%").value shouldBe BigDecimal("0.16")
        }
    }

    describe("BaseMultiplyBinaryOperator") {
        it("1000 * 20% = 200") {
            expressionExecutor.execute("1000 * 20%").value shouldBe BigDecimal("200.00")
        }

        it("1000 * 20.25% = 202.50") {
            expressionExecutor.execute("1000 * 20.25%").value shouldBe BigDecimal("202.50")
        }
    }

    describe("BaseDivideBinaryOperator") {
        it("1000 / 20% = 5000") {
            expressionExecutor.execute("1000 / 20%").value shouldBe BigDecimal("5000.00")
        }

        it("1000 / 20.25% = 4938.27") {
            expressionExecutor.execute("1000 / 20.25%").value shouldBe BigDecimal("4938.27")
        }
    }

    describe("BaseEqualBinaryOperator") {
        it("20% == 0.2 = true") {
            expressionExecutor.execute("20% == 0.2").value shouldBe true
        }

        it("0.2 == 20% = true") {
            expressionExecutor.execute("0.2 == 20%").value shouldBe true
        }

        it("20% == 0.25 = false") {
            expressionExecutor.execute("20% == 0.25").value shouldBe false
        }

        it("0.25 == 20% = false") {
            expressionExecutor.execute("0.25 == 20%").value shouldBe false
        }
    }

    describe("BaseNotEqualBinaryOperator") {
        it("20% != 0.2 = false") {
            expressionExecutor.execute("20% != 0.2").value shouldBe false
        }

        it("0.2 != 20% = false") {
            expressionExecutor.execute("0.2 != 20%").value shouldBe false
        }

        it("20% != 0.25 = true") {
            expressionExecutor.execute("20% != 0.25").value shouldBe true
        }

        it("0.25 != 20% = true") {
            expressionExecutor.execute("0.25 != 20%").value shouldBe true
        }
    }
})
