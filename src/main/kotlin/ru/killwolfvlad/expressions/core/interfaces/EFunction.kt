package ru.killwolfvlad.expressions.core.interfaces

import ru.killwolfvlad.expressions.core.ExpressionExecutor

/**
 * Expression function
 */
interface EFunction : ESymbol {
    /**
     * Execute function
     */
    suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance
}
