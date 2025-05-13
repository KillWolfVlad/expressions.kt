package ru.killwolfvlad.expressions.base.classes

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.base.memory.BaseMemory
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException

class BaseBooleanClassTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    describe("must create instance from string") {
        it("when true") {
            expressionExecutor.execute("true").value shouldBe true
        }

        it("when false") {
            expressionExecutor.execute("false").value shouldBe false
        }
    }

    describe("must create instance from BaseNumberInstance") {
        it("when zero") {
            expressionExecutor.execute("Boolean(0)").value shouldBe false
        }

        it("when non zero") {
            expressionExecutor.execute("Boolean(1)").value shouldBe true
        }
    }

    describe("must create instance from BaseStringInstance") {
        it("when true") {
            expressionExecutor.execute("Boolean(\"true\")").value shouldBe true
        }

        it("when false") {
            expressionExecutor.execute("Boolean(\"false\")").value shouldBe false
        }
    }

    describe("must create instance from BaseBooleanInstance") {
        it("when true") {
            expressionExecutor.execute("Boolean(true)").value shouldBe true
        }

        it("when false") {
            expressionExecutor.execute("Boolean(false)").value shouldBe false
        }
    }

    describe("exceptions") {
        it("must throw wrong count of arguments exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("Boolean(1; 2)")
            }.message shouldBe "Boolean: arguments count must be 1!"
        }

        it("must throw unsupported argument type exception") {
            shouldThrowExactly<EException> {
                BaseBooleanClass().createInstance(expressionExecutor, BaseMemory(), listOf(1))
            }.message shouldBe "Boolean: unsupported argument type Int!"
        }

        it("must throw exception when string is wrong boolean") {
            shouldThrowExactly<IllegalArgumentException> {
                expressionExecutor.execute("Boolean(\"ok\")")
            }.message shouldBe "The string doesn't represent a boolean value: ok"
        }
    }
})
