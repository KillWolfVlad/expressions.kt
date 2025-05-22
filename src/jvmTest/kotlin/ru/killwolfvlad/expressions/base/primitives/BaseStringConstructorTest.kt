package ru.killwolfvlad.expressions.base.primitives

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException

class BaseStringConstructorTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("must create instance from string") {
        expressionExecutor.execute("\"ok\"").value shouldBe "ok"
    }

    it("must create instance from BaseNumberInstance") {
        expressionExecutor.execute("String(20)").value shouldBe "20.00"
    }

    it("must create instance from BaseStringInstance") {
        expressionExecutor.execute("String(\"ok\")").value shouldBe "ok"
    }

    describe("must create instance from BaseBooleanInstance") {
        it("when true") {
            expressionExecutor.execute("String(true)").value shouldBe "true"
        }

        it("when false") {
            expressionExecutor.execute("String(false)").value shouldBe "false"
        }
    }

    describe("exceptions") {
        it("must throw wrong count of arguments exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("String(1; 2)")
            }.message shouldBe "String: arguments count must be 1!"
        }

        it("must throw unsupported argument type exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("String({3})")
            }.message shouldBe "String: unsupported argument type BaseStatementInstance!"
        }
    }
})
