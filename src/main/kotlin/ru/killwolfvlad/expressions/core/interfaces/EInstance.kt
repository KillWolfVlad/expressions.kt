package ru.killwolfvlad.expressions.core.interfaces

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
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance

    /**
     * Apply left unary operator
     */
    suspend fun applyLeftUnaryOperator(operator: ELeftUnaryOperator): EInstance

    /**
     * Apply right unary operator
     */
    suspend fun applyRightUnaryOperator(operator: ERightUnaryOperator): EInstance
}
