package ru.killwolfvlad.expressions.core.interfaces

import ru.killwolfvlad.expressions.core.ExpressionExecutor

/**
 * Expression class
 */
interface EClass : ESymbol {
    /**
     * Create instance
     */
    suspend fun createInstance(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<Any>,
    ): EInstance
}
