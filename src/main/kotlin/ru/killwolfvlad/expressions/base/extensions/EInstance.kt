package ru.killwolfvlad.expressions.base.extensions

import ru.killwolfvlad.expressions.base.memory.BaseMemory
import ru.killwolfvlad.expressions.base.primitives.BaseStatementInstance
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Expand statement
 */
suspend inline fun EInstance.expand(
    expressionExecutor: ExpressionExecutor,
    memory: EMemory,
): EInstance {
    if (this !is BaseStatementInstance) {
        return this
    }

    return expressionExecutor.execute(value, (memory as BaseMemory).copy())
}
