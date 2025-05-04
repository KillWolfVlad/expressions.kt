package ru.killwolfvlad.expressions.core.interfaces

import kotlin.reflect.KClass

/**
 * Expression class instance
 */
interface EClassInstance {
    /**
     * Self class
     */
    val selfClass: KClass<EClass>

    /**
     * Value
     */
    val value: Any

    /**
     * Apply binary operator
     */
    suspend fun applyBinaryOperator(
        other: EClassInstance,
        operator: EBinaryOperator,
    ): EClassInstance

    /**
     * Apply left unary operator
     */
    suspend fun applyLeftUnaryOperator(operator: ELeftUnaryOperator): EClassInstance

    /**
     * Apply right unary operator
     */
    suspend fun applyRightUnaryOperator(operator: ERightUnaryOperator): EClassInstance
}
