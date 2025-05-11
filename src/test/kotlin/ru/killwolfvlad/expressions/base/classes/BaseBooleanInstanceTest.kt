package ru.killwolfvlad.expressions.base.classes

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator

class BaseBooleanInstanceTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("true = true") {
        expressionExecutor.execute("true") shouldBe true
    }

    it("false = false") {
        expressionExecutor.execute("false") shouldBe false
    }

    describe("BaseOrBinaryOperator") {
        it("false || false = false") {
            expressionExecutor.execute("false || false") shouldBe false
        }

        it("false || true = true") {
            expressionExecutor.execute("false || true") shouldBe true
        }

        it("true || false = true") {
            expressionExecutor.execute("true || false") shouldBe true
        }

        it("true || true = true") {
            expressionExecutor.execute("true || true") shouldBe true
        }
    }

    describe("BaseAndBinaryOperator") {
        it("false && false = false") {
            expressionExecutor.execute("false && false") shouldBe false
        }

        it("false && true = false") {
            expressionExecutor.execute("false && true") shouldBe false
        }

        it("true && false = false") {
            expressionExecutor.execute("true && false") shouldBe false
        }

        it("true && true = true") {
            expressionExecutor.execute("true && true") shouldBe true
        }
    }

    describe("BaseEqualBinaryOperator") {
        it("true == true = true") {
            expressionExecutor.execute("true == true") shouldBe true
        }

        it("true == false = false") {
            expressionExecutor.execute("true == false") shouldBe false
        }
    }

    describe("BaseNotEqualBinaryOperator") {
        it("true != true = false") {
            expressionExecutor.execute("true != true") shouldBe false
        }

        it("true == false = true") {
            expressionExecutor.execute("true != false") shouldBe true
        }
    }

    describe("BaseNotLeftUnaryOperator") {
        it("(!true) = false") {
            expressionExecutor.execute("(!true)") shouldBe false
        }

        it("(!false) = true") {
            expressionExecutor.execute("(!false)") shouldBe true
        }
    }

    describe("exceptions") {
        it("must throw argument type must be BaseBooleanInstance exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("true && 20")
            }.message shouldBe "BaseBooleanInstance: argument type must be BaseBooleanInstance!"
        }

        it("must throw unsupported binary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("true + false")
            }.message shouldBe "BaseBooleanInstance: unsupported binary operator type BasePlusBinaryOperator!"
        }

        it("must throw unsupported left unary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("-true")
            }.message shouldBe "BaseBooleanInstance: unsupported left unary operator type BaseMinusLeftUnaryOperator!"
        }

        it("must throw unsupported right unary operator exception") {
            class CustomRightUnaryOperator : ERightUnaryOperator {
                override val description = ""

                override val identifier = "$"
            }

            val customExpressionExecutor =
                ExpressionExecutor(
                    buildBaseExpressionOptions().copy(
                        rightUnaryOperators = listOf(CustomRightUnaryOperator()),
                    ),
                )

            shouldThrowExactly<EException> {
                customExpressionExecutor.execute("true$")
            }.message shouldBe "BaseBooleanInstance: unsupported right unary operator type CustomRightUnaryOperator!"
        }
    }
})
