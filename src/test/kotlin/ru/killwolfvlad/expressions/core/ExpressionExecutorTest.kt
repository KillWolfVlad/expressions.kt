package ru.killwolfvlad.expressions.core

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.exceptions.EException

class ExpressionExecutorTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("must throw exception if function name missing") {
        shouldThrowExactly<EException> {
            expressionExecutor.execute("(10; 30)")
        }.message shouldContain "ExpressionExecutor: missing function name!"
    }

    describe("binary operators priority") {
        it("must use binary operators priority") {
            expressionExecutor
                .execute(
                    """
                    1024 - 2 ** 3 ** 2 + 256 * 2 - 1 >= 768 == 10 > 0 && 0 < 10 || 10 <= 0
                    """.trimIndent(),
                ).value shouldBe true
        }
    }
})
