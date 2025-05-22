package ru.killwolfvlad.expressions.examples

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import java.math.BigDecimal

class FibonacciExampleTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    val baseExpression =
        """
        fun("fibonacci"; "n"; {
          if(var("n") < 0; {
            -1 ** (-var("n") + 1) * fun("fibonacci"; -var("n"))
            }; {
            if(var("n") == 0;
              0; {
              if(var("n") == 1;
                1; {
                fun("fibonacci"; var("n") - 1) + fun("fibonacci"; var("n") - 2)
              })
            })
          })
        })
        """.trimIndent()

    val testCases =
        listOf(
            -25 to 75025,
            -10 to -55,
            -9 to 34,
            -8 to -21,
            -7 to 13,
            -6 to -8,
            -5 to 5,
            -4 to -3,
            -3 to 2,
            -2 to -1,
            -1 to 1,
            0 to 0,
            1 to 1,
            2 to 1,
            3 to 2,
            4 to 3,
            5 to 5,
            6 to 8,
            7 to 13,
            8 to 21,
            9 to 34,
            10 to 55,
            25 to 75025,
        )

    testCases.forEach { testCase ->
        it("fun(\"fibonacci\"; ${testCase.first}) = ${testCase.second}") {
            expressionExecutor
                .execute(
                    """
                    $baseExpression

                    fun("fibonacci"; ${testCase.first})
                    """.trimIndent(),
                ).value
                .let { it as BigDecimal }
                .toInt() shouldBe testCase.second
        }
    }
})
