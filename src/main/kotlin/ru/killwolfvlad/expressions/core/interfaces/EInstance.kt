package ru.killwolfvlad.expressions.core.interfaces

import ru.killwolfvlad.expressions.core.ExpressionExecutor

/**
 * Expression instance
 */
interface EInstance {
    /**
     * Value
     */
    val value: Any

    /**
     * Apply binary operator
     */
    suspend fun applyBinaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance

    /**
     * Apply left unary operator
     */
    suspend fun applyLeftUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance

    /**
     * Apply right unary operator
     */
    suspend fun applyRightUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance
}
