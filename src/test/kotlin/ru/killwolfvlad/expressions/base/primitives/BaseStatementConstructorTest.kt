package ru.killwolfvlad.expressions.base.primitives

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.tokens.EPrimitiveToken

class BaseStatementConstructorTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

    it("must create instance from list of tokens") {
        expressionExecutor.execute("{30}").value shouldBe
            listOf(
                EPrimitiveToken("30", expressionExecutor.options.numberConstructor),
            )
    }

    it("must create instance from BaseStringInstance") {
        expressionExecutor
            .execute(
                """
                Statement("30")
                """.trimIndent(),
            ).value shouldBe
            listOf(
                EPrimitiveToken("30", expressionExecutor.options.numberConstructor),
            )
    }

    it("must create instance from BaseStatementInstance") {
        expressionExecutor
            .execute(
                """
                Statement({30})
                """.trimIndent(),
            ).value shouldBe
            listOf(
                EPrimitiveToken("30", expressionExecutor.options.numberConstructor),
            )
    }

    describe("exceptions") {
        it("must throw wrong count of arguments exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("Statement(30; 20)")
            }.message shouldBe "Statement: arguments count must be 1!"
        }

        it("must throw unsupported argument type exception") {
            shouldThrowExactly<EException> {
                expressionExecutor.execute("Statement(30)")
            }.message shouldBe "Statement: unsupported argument type BaseNumberInstance!"
        }
    }
})
