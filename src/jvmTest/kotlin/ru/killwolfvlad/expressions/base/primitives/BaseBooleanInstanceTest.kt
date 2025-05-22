package ru.killwolfvlad.expressions.base.primitives

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException

class BaseBooleanInstanceTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    describe("BaseAndBinaryOperator") {
        it("false && false = false") {
            expressionExecutor.execute("false && false").value shouldBe false
        }

        it("false && true = false") {
            expressionExecutor.execute("false && true").value shouldBe false
        }

        it("true && false = false") {
            expressionExecutor.execute("true && false").value shouldBe false
        }

        it("true && true = true") {
            expressionExecutor.execute("true && true").value shouldBe true
        }
    }

    describe("BaseOrBinaryOperator") {
        it("false || false = false") {
            expressionExecutor.execute("false || false").value shouldBe false
        }

        it("false || true = true") {
            expressionExecutor.execute("false || true").value shouldBe true
        }

        it("true || false = true") {
            expressionExecutor.execute("true || false").value shouldBe true
        }

        it("true || true = true") {
            expressionExecutor.execute("true || true").value shouldBe true
        }
    }

    describe("BaseEqualBinaryOperator") {
        it("true == true = true") {
            expressionExecutor.execute("true == true").value shouldBe true
        }

        it("true == false = false") {
            expressionExecutor.execute("true == false").value shouldBe false
        }
    }

    describe("BaseNotEqualBinaryOperator") {
        it("true != true = false") {
            expressionExecutor.execute("true != true").value shouldBe false
        }

        it("true == false = true") {
            expressionExecutor.execute("true != false").value shouldBe true
        }
    }

    describe("BaseNotLeftUnaryOperator") {
        it("(!true) = false") {
            expressionExecutor.execute("(!true)").value shouldBe false
        }

        it("(!false) = true") {
            expressionExecutor.execute("(!false)").value shouldBe true
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
            shouldThrowExactly<EException> {
                expressionExecutor.execute("true%")
            }.message shouldBe "BaseBooleanInstance: unsupported right unary operator type BasePercentRightUnaryOperator!"
        }
    }
})
