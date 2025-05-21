package ru.killwolfvlad.expressions.base.primitives

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException

class BaseStringInstanceTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    describe("BasePlusBinaryOperator") {
        it("\"a\" + \"b\" = \"ab\"") {
            expressionExecutor.execute("\"a\" + \"b\"").value shouldBe "ab"
        }
    }

    describe("BaseGreaterBinaryOperator") {
        it("\"a\" > \"a\" = false") {
            expressionExecutor.execute("\"a\" > \"a\"").value shouldBe false
        }

        it("\"a\" > \"b\" = false") {
            expressionExecutor.execute("\"a\" > \"b\"").value shouldBe false
        }

        it("\"b\" > \"a\" = true") {
            expressionExecutor.execute("\"b\" > \"a\"").value shouldBe true
        }
    }

    describe("BaseGreaterOrEqualBinaryOperator") {
        it("\"a\" >= \"a\" = true") {
            expressionExecutor.execute("\"a\" >= \"a\"").value shouldBe true
        }

        it("\"a\" >= \"b\" = false") {
            expressionExecutor.execute("\"a\" >= \"b\"").value shouldBe false
        }

        it("\"b\" >= \"a\" = true") {
            expressionExecutor.execute("\"b\" >= \"a\"").value shouldBe true
        }
    }

    describe("BaseLessBinaryOperator") {
        it("\"a\" < \"a\" = false") {
            expressionExecutor.execute("\"a\" < \"a\"").value shouldBe false
        }

        it("\"a\" < \"b\" = true") {
            expressionExecutor.execute("\"a\" < \"b\"").value shouldBe true
        }

        it("\"b\" < \"a\" = false") {
            expressionExecutor.execute("\"b\" < \"a\"").value shouldBe false
        }
    }

    describe("BaseLessOrEqualBinaryOperator") {
        it("\"a\" <= \"a\" = true") {
            expressionExecutor.execute("\"a\" <= \"a\"").value shouldBe true
        }

        it("\"a\" <= \"b\" = true") {
            expressionExecutor.execute("\"a\" <= \"b\"").value shouldBe true
        }

        it("\"b\" <= \"a\" = false") {
            expressionExecutor.execute("\"b\" <= \"a\"").value shouldBe false
        }
    }

    describe("BaseEqualBinaryOperator") {
        it("\"a\" == \"a\" = true") {
            expressionExecutor.execute("\"a\" == \"a\"").value shouldBe true
        }

        it("\"a\" == \"b\" = false") {
            expressionExecutor.execute("\"a\" == \"b\"").value shouldBe false
        }
    }

    describe("BaseNotEqualBinaryOperator") {
        it("\"a\" != \"a\" = false") {
            expressionExecutor.execute("\"a\" != \"a\"").value shouldBe false
        }

        it("\"a\" != \"b\" = false") {
            expressionExecutor.execute("\"a\" != \"b\"").value shouldBe true
        }
    }

    describe("exceptions") {
        it("must throw argument type must be BaseStringInstance exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("\"a\" + 2")
            }.message shouldBe "BaseStringInstance: argument type must be BaseStringInstance!"
        }

        it("must throw unsupported binary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("\"a\" && \"b\"")
            }.message shouldBe "BaseStringInstance: unsupported binary operator type BaseAndBinaryOperator!"
        }

        it("must throw unsupported left unary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("!\"a\"")
            }.message shouldBe "BaseStringInstance: unsupported left unary operator type BaseNotLeftUnaryOperator!"
        }

        it("must throw unsupported right unary operator exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("\"a\"%")
            }.message shouldBe "BaseStringInstance: unsupported right unary operator type BasePercentRightUnaryOperator!"
        }
    }
})
