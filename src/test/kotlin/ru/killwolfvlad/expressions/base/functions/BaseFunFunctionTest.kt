package ru.killwolfvlad.expressions.base.functions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import java.math.BigDecimal

class BaseFunFunctionTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("must define function") {
        expressionExecutor
            .execute(
                """
                fun("sum"; "a"; "b"; {
                  var("a") + var("b")
                })
                """.trimIndent(),
            ).value shouldBe "sum"
    }

    it("must execute function") {
        expressionExecutor
            .execute(
                """
                fun("sum"; "a"; "b"; {
                  var("a") + var("b")
                })

                fun("sum"; 3; 4)
                """.trimIndent(),
            ).value shouldBe BigDecimal("7.00")
    }

    it("must execute function in function") {
        expressionExecutor
            .execute(
                """
                fun("sum"; "a"; "b"; {
                  var("a") + var("b")
                })

                fun("doubleAndPow2"; "a"; {
                  fun("sum"; var("a"); var("a")) ** 2
                })

                fun("doubleAndPow2"; 5)
                """.trimIndent(),
            ).value shouldBe BigDecimal("100.00")
    }
})
