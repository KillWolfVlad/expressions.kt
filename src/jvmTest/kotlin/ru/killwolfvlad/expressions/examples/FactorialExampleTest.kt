package ru.killwolfvlad.expressions.examples

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import java.math.BigDecimal

class FactorialExampleTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    val baseExpression =
        """
        fun("factorial"; "n"; {
          if(var("n") == 0;
            1; {
            var("n") * fun("factorial"; var("n") - 1)
          })
        })
        """.trimIndent()

    val testCases =
        listOf(
            0 to BigDecimal("1.00"),
            1 to BigDecimal("1.00"),
            2 to BigDecimal("2.00"),
            3 to BigDecimal("6.00"),
            4 to BigDecimal("24.00"),
            5 to BigDecimal("120.00"),
            10 to BigDecimal("3628800.00"),
            15 to BigDecimal("1307674368000.00"),
            20 to BigDecimal("2432902008176640000.00"),
        )

    testCases.forEach { testCase ->
        it("fun(\"factorial\"; ${testCase.first}) = ${testCase.second}") {
            expressionExecutor
                .execute(
                    """
                    $baseExpression

                    fun("factorial"; ${testCase.first})
                    """.trimIndent(),
                ).value shouldBe testCase.second
        }
    }
})
