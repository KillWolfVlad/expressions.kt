package ru.killwolfvlad.expressions.base.classes

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.types.EMemory
import java.math.BigDecimal

class BaseNumberClassTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("must create instance from string") {
        expressionExecutor.execute("20") shouldBe BigDecimal("20.00")
    }

    it("must create instance from BaseNumberInstance") {
        expressionExecutor.execute("Number(20.25)") shouldBe BigDecimal("20.25")
    }

    it("must create instance from BaseStringInstance") {
        expressionExecutor.execute("Number(\"20\")") shouldBe BigDecimal("20.00")
    }

    describe("must create instance from BaseBooleanInstance") {
        it("when true") {
            expressionExecutor.execute("Number(true)") shouldBe BigDecimal("1.00")
        }

        it("when false") {
            expressionExecutor.execute("Number(false)") shouldBe BigDecimal("0.00")
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
                BaseNumberClass().createInstance(EMemory(), listOf(1))
            }.message shouldBe "Number: unsupported argument type Int!"
        }
    }
})
