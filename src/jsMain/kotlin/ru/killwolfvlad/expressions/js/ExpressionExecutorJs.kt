package ru.killwolfvlad.expressions.js

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.EOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.ExpressionParser
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import kotlin.js.Promise

/**
 * Expression executor for JavaScript
 */
@JsExport
class ExpressionExecutorJs(
    val options: EOptions = buildBaseExpressionOptions(),
) {
    private val expressionExecutor = ExpressionExecutor(options)

    val parser: ExpressionParser = expressionExecutor.parser

    fun execute(expression: String): Promise<EInstance> =
        GlobalScope.promise {
            expressionExecutor.execute(expression)
        }
}
