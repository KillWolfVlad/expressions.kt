package ru.killwolfvlad.expressions.base.functions

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import java.math.BigDecimal

class BaseIfFunctionTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("if(true; 1; 2) = 1") {
        expressionExecutor.execute("if(true; 1; 2)") shouldBe BigDecimal("1.00")
    }

    it("if(false; 1; 2) = 2") {
        expressionExecutor.execute("if(false; 1; 2)") shouldBe BigDecimal("2.00")
    }

    describe("exceptions") {
        it("must throw wrong count of arguments exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("if(true; 1)")
            }.message shouldBe "if: arguments count must be 3!"
        }

        it("must throw argument type must be BaseBooleanInstance exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("if(1; 2; 3)")
            }.message shouldBe "if: argument type must be BaseBooleanInstance!"
        }
    }
})
