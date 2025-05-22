package ru.killwolfvlad.expressions.core.symbols

import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Expression primitive constructor
 */
sealed interface EPrimitiveConstructor<T> : EFunction {
    /**
     * Create instance
     */
    suspend fun createInstance(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        value: T,
    ): EInstance
}
