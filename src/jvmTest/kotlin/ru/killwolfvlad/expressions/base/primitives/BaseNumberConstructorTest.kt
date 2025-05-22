package ru.killwolfvlad.expressions.base.primitives

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import java.math.BigDecimal

class BaseNumberConstructorTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    describe("must create instance from string") {
        it("20") {
            expressionExecutor.execute("20").value shouldBe BigDecimal("20.00")
        }

        it("20.25") {
            expressionExecutor.execute("20.25").value shouldBe BigDecimal("20.25")
        }

        it(".20") {
            expressionExecutor.execute(".20").value shouldBe BigDecimal("0.20")
        }

        it("20.") {
            expressionExecutor.execute("20.").value shouldBe BigDecimal("20.00")
        }

        it("20.987654321") {
            expressionExecutor.execute("20.987654321").value shouldBe BigDecimal("20.99")
        }
    }

    it("must create instance from BaseNumberInstance") {
        expressionExecutor.execute("Number(20.25)").value shouldBe BigDecimal("20.25")
    }

    it("must create instance from BaseStringInstance") {
        expressionExecutor.execute("Number(\"20\")").value shouldBe BigDecimal("20.00")
    }

    describe("must create instance from BaseBooleanInstance") {
        it("when true") {
            expressionExecutor.execute("Number(true)").value shouldBe BigDecimal("1.00")
        }

        it("when false") {
            expressionExecutor.execute("Number(false)").value shouldBe BigDecimal("0.00")
        }
    }

    describe("exceptions") {
        it("must throw wrong count of arguments exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("Number(1; 2)")
            }.message shouldBe "Number: arguments count must be 1!"
        }

        it("must throw unsupported argument type exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("Number({30})")
            }.message shouldBe "Number: unsupported argument type BaseStatementInstance!"
        }
    }
})
