package ru.killwolfvlad.expressions.examples

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import java.math.BigDecimal

class EvalExampleTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("eval") {
        expressionExecutor
            .execute(
                """
                # be careful, because this eval is not isolated and can execute other functions and modify variables
                fun("eval"; "s"; {
                  # use if to expand {}
                  if(true; Statement(var("s")); 0) # to convert string to {}
                })

                fun("eval"; "10 + 20")
                """.trimIndent(),
            ).value shouldBe BigDecimal("30.00")
    }
})
