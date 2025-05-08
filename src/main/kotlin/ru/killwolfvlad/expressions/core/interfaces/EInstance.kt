package ru.killwolfvlad.expressions.core.interfaces

import ru.killwolfvlad.expressions.core.types.EMemory

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
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance

    /**
     * Apply left unary operator
     */
    suspend fun applyLeftUnaryOperator(
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance

    /**
     * Apply right unary operator
     */
    suspend fun applyRightUnaryOperator(
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance
}
