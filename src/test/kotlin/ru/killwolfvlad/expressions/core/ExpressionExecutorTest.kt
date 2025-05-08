package ru.killwolfvlad.expressions.core

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildExpressionBaseOptions
import java.math.BigDecimal

class ExpressionExecutorTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildExpressionBaseOptions())

    it(" - 1 + 2 * 3 = 3") {
        expressionExecutor.execute("- 1 + 2 * 3") shouldBe BigDecimal(5)
    }

    it(" - 1 * 2 + 3 = 1") {
        expressionExecutor.execute("- 1 * 2 + 3") shouldBe BigDecimal(1)
    }

    it("1 + 2 ; 3 + 4") {
        expressionExecutor.execute(
            """
            Number(23;56)
            """.trimIndent(),
        ) shouldBe BigDecimal(23)
    }

    it("test var") {
        expressionExecutor.execute(
            """
            var("a"; -2 - (+1))
            var("b"; +(+3))
            var("a") ** var("b")
            """.trimIndent(),
        ) shouldBe BigDecimal("-27")
    }
})
