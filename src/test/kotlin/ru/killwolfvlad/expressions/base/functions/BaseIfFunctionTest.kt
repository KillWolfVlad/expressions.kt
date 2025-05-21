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

    it("must return then if condition true") {
        expressionExecutor.execute("if(true; 1; 2)").value shouldBe BigDecimal("1.00")
    }

    it("must return else if condition false") {
        expressionExecutor.execute("if(false; 1; 2)").value shouldBe BigDecimal("2.00")
    }

    it("must expand condition") {
        expressionExecutor
            .execute(
                """
                if({10 == 5 * 2}; {10 + 30}; {10 + "2"})
                """.trimIndent(),
            ).value shouldBe BigDecimal("40.00")
    }

    it("must expand then") {
        expressionExecutor
            .execute(
                """
                if(true; {10 + 30}; {10 + "2"})
                """.trimIndent(),
            ).value shouldBe BigDecimal("40.00")
    }

    it("must expand else") {
        expressionExecutor
            .execute(
                """
                if(!true; {10 + "2"}; {10 + 30})
                """.trimIndent(),
            ).value shouldBe BigDecimal("40.00")
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
