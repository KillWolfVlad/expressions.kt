package ru.killwolfvlad.expressions.core

import io.kotest.core.spec.style.DescribeSpec
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions

class ExpressionExecutorTest : DescribeSpec({
    val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())
})
