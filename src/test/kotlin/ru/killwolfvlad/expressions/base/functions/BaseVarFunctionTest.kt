package ru.killwolfvlad.expressions.base.functions

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import java.math.BigDecimal

class BaseVarFunctionTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("must set variable") {
        expressionExecutor.execute("var(\"a\"; 1)").value shouldBe BigDecimal("1.00")
    }

    it("must set and get variable") {
        expressionExecutor.execute("var(\"a\"; 1); var(\"a\")").value shouldBe BigDecimal("1.00")
    }

    it("must read captured variable") {
        expressionExecutor
            .execute(
                """
                var("a"; 5)

                if(true; {
                  var("a")
                  };
                  false
                )
                """.trimIndent(),
            ).value shouldBe BigDecimal("5.00")
    }

    it("must reassign captured variable") {
        expressionExecutor
            .execute(
                """
                var("a"; 1)

                if(true; {
                  var("a"; 3)
                  };
                  false
                )

                var("a")
                """.trimIndent(),
            ).value shouldBe BigDecimal("3.00")
    }

    describe("exceptions") {
        it("must throw wrong count of arguments exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("var(\"a\"; 1; 2)")
            }.message shouldBe "var: arguments count must be 1, 2!"
        }

        it("must throw argument type must be BaseStringInstance exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("var(1)")
            }.message shouldBe "var: argument type must be BaseStringInstance!"
        }

        it("must throw variable with name is not defined exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("var(\"a\")")
            }.message shouldBe "var: variable with name a is not defined!"
        }

        it("must throw exception when trying access to variable from other scope") {
            shouldThrow<EException> {
                expressionExecutor
                    .execute(
                        """
                        var("a"; 1)

                        if(true; {
                          var("d"; 3)
                          };
                          false
                        )

                        var("d")
                        """.trimIndent(),
                    ).value
            }.message shouldBe "var: variable with name d is not defined!"
        }

        it("must throw exception when memory is not BaseMemory") {
            class CustomMemory : EMemory

            shouldThrowExactly<ClassCastException> {
                expressionExecutor.execute("var(\"a\"; 1)", CustomMemory())
            }
        }

        it("must throw exception when variable reassign wit different type") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute(
                    """
                    var("a"; 1)
                    var("a"; "1")
                    """.trimIndent(),
                )
            }.message shouldBe "var: can't reassign variable with different type!"
        }
    }
})
