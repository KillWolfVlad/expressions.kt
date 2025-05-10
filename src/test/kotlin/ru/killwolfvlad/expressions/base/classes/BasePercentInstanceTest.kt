package ru.killwolfvlad.expressions.base.classes

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import java.math.BigDecimal

class BasePercentInstanceTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("20% = 0.20") {
        expressionExecutor.execute("20%") shouldBe BigDecimal("0.2000")
    }

    it("20.25% = 0.2025") {
        expressionExecutor.execute("20.25%") shouldBe BigDecimal("0.2025")
    }

    it("1000 + 20% * 4 = 1000.80") {
        expressionExecutor.execute("1000 + 20% * 4") shouldBe BigDecimal("1000.80")
    }

    describe("BasePlusBinaryOperator") {
        it("1000 + 20% = 1200") {
            expressionExecutor.execute("1000 + 20%") shouldBe BigDecimal("1200.00")
        }

        it("1000 + 20.25% = 1202.50") {
            expressionExecutor.execute("1000 + 20.25%") shouldBe BigDecimal("1202.50")
        }

        it("20% + 1000 = 1000.20") {
            expressionExecutor.execute("20% + 1000") shouldBe BigDecimal("1000.20")
        }

        it("20% + 20% = 0.24") {
            expressionExecutor.execute("20% + 20%") shouldBe BigDecimal("0.24")
        }
    }

    describe("BaseMinusBinaryOperator") {
        it("1000 - 20% = 800") {
            expressionExecutor.execute("1000 - 20%") shouldBe BigDecimal("800.00")
        }

        it("1000 - 20.25% = 800") {
            expressionExecutor.execute("1000 - 20.25%") shouldBe BigDecimal("797.50")
        }

        it("20% - 1000 = 999.80") {
            expressionExecutor.execute("20% - 1000") shouldBe BigDecimal("-999.80")
        }

        it("20% - 20% = 0.16") {
            expressionExecutor.execute("20% - 20%") shouldBe BigDecimal("0.16")
        }
    }

    describe("BaseMultiplyBinaryOperator") {
        it("1000 * 20% = 200") {
            expressionExecutor.execute("1000 * 20%") shouldBe BigDecimal("200.00")
        }

        it("1000 * 20.25% = 202.50") {
            expressionExecutor.execute("1000 * 20.25%") shouldBe BigDecimal("202.50")
        }
    }

    describe("BaseDivideBinaryOperator") {
        it("1000 / 20% = 5000") {
            expressionExecutor.execute("1000 / 20%") shouldBe BigDecimal("5000.00")
        }

        it("1000 / 20.25% = 4938.27") {
            expressionExecutor.execute("1000 / 20.25%") shouldBe BigDecimal("4938.27")
        }
    }
})
