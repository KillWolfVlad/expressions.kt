package ru.killwolfvlad.expressions.base.primitives

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException

class BaseStatementInstanceTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    describe("exceptions") {
        it("must throw unsupported binary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("{10} + {20}")
            }.message shouldBe "BaseStatementInstance: unsupported binary operator type BasePlusBinaryOperator!"
        }

        it("must throw unsupported left unary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("+{30}")
            }.message shouldBe "BaseStatementInstance: unsupported left unary operator type BasePlusLeftUnaryOperator!"
        }

        it("must throw unsupported right unary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("{30}%")
            }.message shouldBe "BaseStatementInstance: unsupported right unary operator type BasePercentRightUnaryOperator!"
        }
    }
})
